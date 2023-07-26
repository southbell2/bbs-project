package proj.bbs.config;

import static proj.bbs.constants.Routes.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import proj.bbs.security.filter.RefreshTokenValidatorFilter;
import proj.bbs.security.filter.TokenGeneratorFilter;
import proj.bbs.security.filter.AccessTokenValidatorFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccessTokenValidatorFilter accessTokenValidatorFilter;
    private final RefreshTokenValidatorFilter refreshTokenValidatorFilter;
    private final TokenGeneratorFilter tokenGeneratorFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(REFRESH_TOKEN.getPath()).authenticated()
                .requestMatchers(LOGIN.getPath(), USERINFO.getPath(), UPDATE_USERINFO.getPath(),
                    UPDATE_PASSWORD.getPath(), DELETE_USER.getPath()).authenticated()
                .requestMatchers(SIGNUP.getPath()).permitAll()
                .requestMatchers(NEW_POST.getPath()).authenticated())
            .addFilterBefore(refreshTokenValidatorFilter, BasicAuthenticationFilter.class)
            .addFilterBefore(accessTokenValidatorFilter, BasicAuthenticationFilter.class)
            .addFilterAfter(tokenGeneratorFilter, BasicAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
