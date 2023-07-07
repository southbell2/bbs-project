package proj.bbs.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.UserMapper;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdatePasswordDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

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
        User user = userRepository.findByEmail(email);
        return userMapper.userToUserInfoDto(user);
    }

    @Transactional
    public void updateUserInfo(UpdateUserInfoDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        user.updateUserInfo(userDTO);
    }

    /**
     *  비밀번호 변경은 현재 비밀번호를 같이 보내서 사용자가 맞는지 한 번더 확인한다.
     */
    @Transactional
    public void updatePassword(String email, UpdatePasswordDTO updatePasswordDTO) {
        User user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(updatePasswordDTO.getNowPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        user.updatePassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
    }

}
