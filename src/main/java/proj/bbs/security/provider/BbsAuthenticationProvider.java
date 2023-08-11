package proj.bbs.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import proj.bbs.security.principal.UserPrincipal;
import proj.bbs.user.domain.User;
import proj.bbs.user.domain.UserRole;
import proj.bbs.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
            user = userRepository.findByEmailWithRole(email);
        } catch (EmptyResultDataAccessException e) {
            log.info("이메일로 회원을 찾을 수 없습니다 , email = {}", email);
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getEmail());
            return new UsernamePasswordAuthenticationToken(userPrincipal, rawPassword, getGrantedAuthorities(user.getRoles()));
        } else {
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

    }

    private List<GrantedAuthority> getGrantedAuthorities(Collection<UserRole> authorities) {
        return authorities.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
