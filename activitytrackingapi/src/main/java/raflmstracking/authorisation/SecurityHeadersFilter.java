package raflmstracking.authorisation;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RISK-12 fix: standardni HTTP security header-i na sve odgovore tracking API-ja.
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
