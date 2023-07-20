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
import proj.bbs.user.filter.JWTTokenGeneratorFilter;
import proj.bbs.user.filter.JWTTokenValidatorFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;
    private final JWTTokenGeneratorFilter jwtTokenGeneratorFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/login", "/userinfo", "/update-userinfo", "/update-password",
                    "/delete-user").authenticated()
                .requestMatchers("/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/post").authenticated())
            .addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
            .addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
