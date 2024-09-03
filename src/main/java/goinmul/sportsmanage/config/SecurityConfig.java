package goinmul.sportsmanage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable()) //no cache 비활성화
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self' https://stackpath.bootstrapcdn.com https://cdn.jsdelivr.net https://fonts.googleapis.com " +
                                        "https://fonts.gstatic.com https://cdnjs.cloudflare.com https://cdn.iamport.kr https://service.iamport.kr https://online-pay.kakao.com " +
                                        "https://dapi.kakao.com https://t1.daumcdn.net https://map.daumcdn.net")
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
