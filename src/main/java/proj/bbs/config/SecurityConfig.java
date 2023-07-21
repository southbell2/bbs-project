package proj.bbs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import proj.bbs.security.filter.TokenGeneratorFilter;
import proj.bbs.security.filter.TokenValidatorFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenValidatorFilter tokenValidatorFilter;
    private final TokenGeneratorFilter tokenGeneratorFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/login", "/userinfo", "/update-userinfo", "/update-password",
                    "/delete-user").authenticated()
                .requestMatchers("/signup", "/hello").permitAll()
                .requestMatchers(HttpMethod.POST, "/post").authenticated())
            .addFilterBefore(tokenValidatorFilter, BasicAuthenticationFilter.class)
            .addFilterAfter(tokenGeneratorFilter, BasicAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
