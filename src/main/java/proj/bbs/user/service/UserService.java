package proj.bbs.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.exception.NotFoundException;
import proj.bbs.user.UserMapper;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.SignUpUserDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpUserDTO userDTO) {
        String rawPassword = userDTO.getPassword();
        String hashPassword = passwordEncoder.encode(rawPassword);
        userDTO.setPassword(hashPassword);

        User user = userMapper.signUpDtoToUser(userDTO);
        userRepository.saveUser(user);
    }

    public UserInfoDTO getUserInfo(String email) {
        List<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("요청한 회원을 찾을 수 없습니다");
        }

        return userMapper.userToUserInfoDto(user.get(0));
    }
}
