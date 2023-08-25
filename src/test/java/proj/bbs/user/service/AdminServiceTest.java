package proj.bbs.user.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.domain.RoleType;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.PagedUserDTO;
import proj.bbs.user.service.dto.SignUpUserDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class AdminServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @Autowired
    AdminService adminService;

    @Test
    public void ADMIN_회원가입() {
        //given
        SignUpUserDTO userDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setPassword("12345");

        //when
        adminService.signUpAdmin(userDTO);

        //then
        User adminUser = userRepository.findByEmailWithRole(email);
        boolean matchForTrue = adminUser.getUserRoles().stream()
                .anyMatch(u -> u.getRole().equals(RoleType.ROLE_ADMIN));
        assertThat(matchForTrue).isTrue();
    }

    @Test
    public void UserRole_추가_삭제() {
        //추가
        //Given
        SignUpUserDTO userDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setPassword("12345");
        userService.signUp(userDTO);
        Long id = userRepository.findByEmail(email).getId();

        //when
        adminService.addUserRole(id, "ADMIN");
        em.clear();

        //then
        boolean matchForTrue  = userRepository.findByIdWithRole(id)
                .getUserRoles()
                .stream()
                .anyMatch(u -> u.getRole().equals(RoleType.ROLE_ADMIN));
        assertThat(matchForTrue).isTrue();

        //삭제
        //when
        adminService.deleteUserRole(id, "USER");
        em.flush();
        em.clear();

        //then
        boolean matchForFalse  = userRepository.findByIdWithRole(id)
                .getUserRoles()
                .stream()
                .anyMatch(u -> u.getRole().equals(RoleType.ROLE_USER));
        assertThat(matchForFalse).isFalse();
    }
}
