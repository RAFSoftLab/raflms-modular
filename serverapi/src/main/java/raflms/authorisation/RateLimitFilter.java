package raflms.authorisation;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RISK-15 fix: jednostavan in-memory rate limiter za student endpoint-e koji
 * ne zahtevaju token (authorizeforasignment, upload/studentproject).
 *
 * Dozvoljava MAX_REQUESTS zahteva po IP adresi u prozoru od WINDOW_SECONDS sekundi.
 * Exceeding the limit returns HTTP 429 Too Many Requests.
 *
 * Napomena: in-memory implementacija nije pogodna za multi-instance deployment.
 * Za produkciju sa vise instanci treba Redis-based rate limiter (npr. Bucket4j + Redis).
 */
public class RateLimitFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    /** Maksimalan broj zahteva po IP-u u prozoru. */
    private static final int MAX_REQUESTS = 30;

    /** Duzina prozora u sekundama. */
    private static final long WINDOW_SECONDS = 60;

    /** IP -> (count, windowStart) */
    private final ConcurrentHashMap<String, long[]> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String clientIp = getClientIp(httpReq);
        long now = Instant.now().getEpochSecond();

        long[] state = requestCounts.compute(clientIp, (ip, prev) -> {
            if (prev == null || now - prev[1] >= WINDOW_SECONDS) {
                return new long[]{1, now};   // novi prozor
            }
            prev[0]++;
            return prev;
        });

        if (state[0] > MAX_REQUESTS) {
            log.warn("Rate limit prekoracen za IP {}: {} zahteva u {} s", clientIp, state[0], WINDOW_SECONDS);
            httpRes.setStatus(429);
            httpRes.setContentType("application/json");
            httpRes.getWriter().write("{\"error\":\"Previse zahteva. Pokusajte ponovo za malo.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public void destroy() {
        requestCounts.clear();
    }
}
