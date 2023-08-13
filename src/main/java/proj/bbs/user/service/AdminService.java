package proj.bbs.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.UserMapper;
import proj.bbs.user.domain.User;
import proj.bbs.user.domain.UserRole;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UserInfoAdminDTO;

import static proj.bbs.user.domain.RoleType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpAdmin(SignUpUserDTO userDTO) {
        UserRole userRole = new UserRole(ROLE_USER);
        UserRole adminRole = new UserRole(ROLE_ADMIN);

        User user = User.createUser(userDTO, passwordEncoder, userRole, adminRole);
        userRepository.saveUser(user);
    }

    @Transactional
    public void addUserRole(Long userId, String role) {
        User user = userRepository.findByIdWithRole(userId);
        UserRole userRole = new UserRole(valueOf("ROLE_" + role));
        user.addUserRole(userRole);
        userRepository.saveUserRole(userRole);
    }

    @Transactional
    public void deleteUserRole(Long userId, String role) {
        User user = userRepository.findByIdWithRole(userId);user.getRoles().stream()
                .filter(userRole -> userRole.getRole().equals(valueOf("ROLE_" + role)))
                .forEach(userRepository::deleteUserRole);
    }

    public UserInfoAdminDTO getUserInfoByAdmin(Long userId) {
        User user = userRepository.findByIdWithRole(userId);
        return userMapper.userToUserInfoAdminDto(user);
    }
}
