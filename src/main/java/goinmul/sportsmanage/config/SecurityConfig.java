package goinmul.sportsmanage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     List<String> cspDirectives = new ArrayList<>(List.of(
            "http://t1.daumcdn.net",
            "http://dapi.kakao.com",
             "http://mts.daumcdn.net",
            "https://stackpath.bootstrapcdn.com",
            "https://cdn.jsdelivr.net",
            "https://fonts.googleapis.com",
            "https://fonts.gstatic.com",
            "https://cdnjs.cloudflare.com",
            "https://cdn.iamport.kr",
            "https://service.iamport.kr",
            "https://online-pay.kakao.com",
             "https://online-payment.kakaopay.com;"

    ));


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    //카카오 지도 때문에 만듬
    public SecurityFilterChain cspWithUnsafeInline(HttpSecurity http) throws Exception {
        List<String> newDirectives = new ArrayList<>();
        newDirectives.add("default-src 'self'");
        newDirectives.addAll(cspDirectives);
        newDirectives.add("style-src 'self' 'unsafe-inline'");
        newDirectives.addAll(cspDirectives);

        http
                .securityMatcher("/reservation/**")  // 특정 URL 패턴에만 적용
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(String.join(" ", newDirectives))
                        )
                )
                .csrf((csrf) -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        List<String> newDirectives = new ArrayList<>();
        newDirectives.add("default-src 'self'");
        newDirectives.addAll(cspDirectives);

        http
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable()) //no cache 비활성화
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(String.join(" ", newDirectives))
                        )
                )
                .csrf((csrf) -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()));
        return http.build();
    }




    /*
    @Autowired
    public void configure(AuthenticationManagerBuilder builder) {
        builder.eraseCredentials(false);
    }

     */



}
