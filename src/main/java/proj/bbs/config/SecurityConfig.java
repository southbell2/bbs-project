package proj.bbs.config;

import static org.springframework.http.HttpMethod.*;
import static proj.bbs.constants.Routes.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import proj.bbs.security.IPAuthorizationManager;
import proj.bbs.security.filter.ExceptionHandlerFilter;
import proj.bbs.security.filter.RefreshTokenValidatorFilter;
import proj.bbs.security.filter.TokenGeneratorFilter;
import proj.bbs.security.filter.AccessTokenValidatorFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccessTokenValidatorFilter accessTokenValidatorFilter;
    private final RefreshTokenValidatorFilter refreshTokenValidatorFilter;
    private final TokenGeneratorFilter tokenGeneratorFilter;
    private final IPAuthorizationManager<RequestAuthorizationContext> ipAuthorizationManager;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(REFRESH_TOKEN.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(USERINFO.getPath(), UPDATE_USERINFO.getPath(),
                                UPDATE_PASSWORD.getPath(), DELETE_USER.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(SIGNUP.getPath(), LOGIN.getPath()).permitAll()
                        .requestMatchers(ADD_USER_ROLE.getPath(), DELETE_USER_ROLE.getPath(), USERINFO_ADMIN.getPath(),
                                DELETE_USER_ADMIN.getPath(), USERINFO_LIST.getPath()).hasRole("ADMIN")
                        .requestMatchers(SIGNUP_ADMIN.getPath()).access(ipAuthorizationManager)
                        .requestMatchers(NEW_POST.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(UPDATE_POST.getMethod(), UPDATE_POST.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(DELETE_POST.getMethod(), DELETE_POST.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(MY_POSTS.getMethod(), MY_POSTS.getPath()).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(GET, SHOW_POST.getPath(), PAGED_POSTS.getPath()).permitAll())
                .addFilterBefore(accessTokenValidatorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(refreshTokenValidatorFilter, AccessTokenValidatorFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), RefreshTokenValidatorFilter.class)
                .addFilterAfter(tokenGeneratorFilter, BasicAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
