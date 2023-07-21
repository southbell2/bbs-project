package proj.bbs.config;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import proj.bbs.exception.UnauthorizedException;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class BbsAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        User user;
        try {
            user = userRepository.findByEmail(email);
        } catch (EmptyResultDataAccessException e) {
            log.info("이메일로 회원을 찾을 수 없습니다 , email = {}", email);
            throw new UnauthorizedException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getEmail());
            return new UsernamePasswordAuthenticationToken(userPrincipal, rawPassword, authorities);
        } else {
            throw new UnauthorizedException("이메일 또는 비밀번호가 틀렸습니다.");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
