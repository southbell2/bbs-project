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

    @Transactional
    public void updatePassword(String email, UpdatePasswordDTO updatePasswordDTO) {
        User user = userRepository.findByEmail(email);
        user.updatePassword(updatePasswordDTO.getNewPassword(), updatePasswordDTO.getNowPassword(), passwordEncoder);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        userRepository.deleteUser(user);
    }

}
