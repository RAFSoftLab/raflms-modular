package raflmstracking.authorisation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// RISK-06 fix: svi GET endpoint-i koji izlazu podatke o studentima su zasticeni tokenom.
//
// Nezasticen (student plugin salje, bez Authorization headera):
//   POST /tracking/events/batch   - plugin salje batch dogadjaja tokom ispita
//
// Zasticeni (samo nastavnik/admin):
//   GET /tracking/events/*        - citanje dogadjaja po studentu ili sesiji
//   GET /tracking/sessions/*      - citanje sesija
//   GET /tracking/struggles/*     - citanje borbi
@Configuration
public class FilterConfig {

    @Value("${raflms.auth.token}")
    private String authToken;

    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> trackingAuthFilter() {
        FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenAuthenticationFilter(authToken));
        registrationBean.addUrlPatterns(
            "/tracking/events/student/*",
            "/tracking/events/session/*",
            "/tracking/sessions/student/*",
            "/tracking/sessions/*",
            "/tracking/struggles/*"
        );
        return registrationBean;
    }
}
