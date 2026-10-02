package raflms.authorisation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // RISK-03 + RISK-04 fix:
    // - Token se vise ne hardkoduje u kodu vec dolazi iz env varijable RAF_AUTH_TOKEN
    //   (definisana u application.properties kao raflms.auth.token=${RAF_AUTH_TOKEN}).
    // - URL pattern je promenjen sa nepostojece putanje na tacne admin/nastavnik endpoint-e.
    //
    // Nezasticeni (student-facing) endpoint-i:
    //   POST /student/submission/authorizeforasignment  - student zapocinje ispit
    //   POST /project/upload/studentproject             - student predaje rad
    //
    // Zasticeni (samo nastavnik/admin):
    //   /student/*           osim /student/submission/authorizeforasignment
    //   /subject/*
    //   /test/*
    //   /project/upload/assignment
    //   /project/download/studentassignment/*
    //   /student/submission/allfortest
    @Value("${raflms.auth.token}")
    private String authToken;

    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> tokenAuthenticationFilter() {
        FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenAuthenticationFilter(authToken));
        registrationBean.addUrlPatterns(
            "/student/add",
            "/student/all",
            "/student/registerfortest",
            "/student/registerallfortest",
            "/subject/*",
            "/test/*",
            "/project/upload/assignment",
            "/project/download/studentassignment/*",
            "/student/submission/allfortest"
        );
        return registrationBean;
    }
}
