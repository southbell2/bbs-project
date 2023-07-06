package proj.bbs.config;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        List<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("회원 E-mail을 찾을 수 없습니다.");
        } else {
            foundEmail = user.get(0).getEmail();
            foundPassword = user.get(0).getPassword();
        }

        return new org.springframework.security.core.userdetails.User(foundEmail, foundPassword,
            new ArrayList<>());
    }
}
