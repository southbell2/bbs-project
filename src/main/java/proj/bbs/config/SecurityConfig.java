package proj.bbs.config;

import static proj.bbs.constants.Routes.*;
import static proj.bbs.user.domain.RoleType.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
                        .requestMatchers(ADD_USER_ROLE.getPath(), DELETE_USER_ROLE.getPath()).permitAll()  //테스트를 위해 잠시 퍼밋올
                        .requestMatchers(NEW_POST.getPath()).hasAnyRole("ADMIN", "USER"))
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
