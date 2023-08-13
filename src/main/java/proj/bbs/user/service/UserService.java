package proj.bbs.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.UserMapper;
import proj.bbs.user.service.dto.*;
import proj.bbs.user.domain.RoleType;
import proj.bbs.user.domain.User;
import proj.bbs.user.domain.UserRole;
import proj.bbs.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpUserDTO userDTO) {
        UserRole userRole = new UserRole(RoleType.ROLE_USER);
        User user = User.createUser(userDTO, passwordEncoder, userRole);
        userRepository.saveUser(user);
    }

    public UserInfoDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId);
        return userMapper.userToUserInfoDto(user);
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoDTO userDTO) {
        User user = userRepository.findById(userId);
        user.updateUserInfo(userDTO);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        User user = userRepository.findById(userId);
        user.updatePassword(updatePasswordDTO.getNewPassword(), updatePasswordDTO.getNowPassword(), passwordEncoder);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        userRepository.deleteUser(user);
    }

}
