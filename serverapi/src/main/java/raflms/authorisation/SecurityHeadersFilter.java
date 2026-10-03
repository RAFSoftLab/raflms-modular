package raflms.authorisation;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RISK-12 fix: dodaje standardne HTTP security header-e na sve odgovore.
 *
 * X-Content-Type-Options  — sprecava MIME-type sniffing (npr. HTML u ZIP response-u)
 * X-Frame-Options          — sprecava clickjacking (ucitavanje u iframe)
 * Referrer-Policy          — ne salje Referer header na cross-origin zahteve
 * Cache-Control            — ne kesiraj API odgovore (sadrze podatke o studentima)
 * X-XSS-Protection         — zastita za starije browsere
 */
@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("Referrer-Policy", "no-referrer");
        httpResponse.setHeader("Cache-Control", "no-store");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        chain.doFilter(request, response);
    }
}
