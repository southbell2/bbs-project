package proj.bbs.config;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BbsUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String foundEmail, foundPassword = null;

        try {
            User user = userRepository.findByEmail(email);
            foundEmail = user.getEmail();
            foundPassword = user.getPassword();
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("회원 E-mail을 찾을 수 없습니다.");
        }

        return new org.springframework.security.core.userdetails.User(foundEmail, foundPassword,
            new ArrayList<>());
    }
}
