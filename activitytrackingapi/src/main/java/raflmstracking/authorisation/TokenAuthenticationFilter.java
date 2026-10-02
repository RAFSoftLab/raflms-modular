package raflmstracking.authorisation;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// RISK-06 fix: svi GET endpoint-i koji izlazu podatke o studentima sada zahtevaju
// Bearer token. Token se injektuje iz raflms.auth.token (env varijabla RAF_AUTH_TOKEN).
public class TokenAuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final String validToken;

    public TokenAuthenticationFilter(String validToken) {
        this.validToken = validToken;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.equals("Bearer " + validToken)) {
            log.warn("Odbijen neautorizovan zahtev: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
