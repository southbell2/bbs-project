package proj.bbs.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.exception.UnauthorizedException;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdatePasswordDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 회원_가입_성공() {
        //Given
        SignUpUserDTO userDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setPassword("12345");

        //When
        userService.signUp(userDTO);
        User user = userRepository.findByEmail(email);

        //Then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void 회원_가입_실패() {
        //Given
        SignUpUserDTO user = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        user.setEmail(email);
        user.setNickname(nickname);
        user.setPassword("12345");
        userService.signUp(user);

        //When && Then
        assertThatThrownBy(() -> userService.signUp(user))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void 회원_정보_얻기() {
        //given
        SignUpUserDTO userDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setPassword("12345");
        userService.signUp(userDTO);
        User user = userRepository.findByEmail(email);

        //when
        UserInfoDTO userInfo = userService.getUserInfo(user.getId());

        //then
        assertThat(userInfo.getEmail()).isEqualTo(email);
        assertThat(userInfo.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void 회원_정보_얻기_실패() {
        //given
        Long id = 1L;

        //when && then
        assertThat(userService.getUserInfo(id)).isNull();

    }

    @Test
    public void 회원_정보_수정() {
        //given
        SignUpUserDTO signUpUserDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        signUpUserDTO.setEmail(email);
        signUpUserDTO.setNickname(nickname);
        signUpUserDTO.setPassword("12345");
        userService.signUp(signUpUserDTO);
        User user = userRepository.findByEmail(email);

        nickname = "Spring";
        UpdateUserInfoDTO userInfoDTO = new UpdateUserInfoDTO();
        userInfoDTO.setNickname(nickname);

        //when
        userService.updateUserInfo(user.getId(), userInfoDTO);

        //then
        assertThat(user.getNickname()).isEqualTo(nickname);

    }

    @Test
    public void 비밀번호_수정() {
        //given
        SignUpUserDTO signUpUserDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nowPassword = "12345";
        signUpUserDTO.setEmail(email);
        signUpUserDTO.setNickname("Terry");
        signUpUserDTO.setPassword(nowPassword);
        userService.signUp(signUpUserDTO);
        User user = userRepository.findByEmail(email);

        String newPassword = "qwerasdf";
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setNowPassword(nowPassword);
        passwordDTO.setNewPassword(newPassword);

        //when
        userService.updatePassword(user.getId(), passwordDTO);

        //then
        assertThat(passwordEncoder.matches(newPassword, user.getPassword())).isTrue();
    }

    @Test
    public void 비밀번호_수정_실패() {
        //given
        SignUpUserDTO signUpUserDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nowPassword = "12345";
        signUpUserDTO.setEmail(email);
        signUpUserDTO.setNickname("Terry");
        signUpUserDTO.setPassword(nowPassword);
        userService.signUp(signUpUserDTO);
        User user = userRepository.findByEmail(email);

        //현재 비밀번호를 틀린 비밀번호로 입력
        String newPassword = "qwerasdf";
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setNowPassword(newPassword);
        passwordDTO.setNewPassword(newPassword);

        //when && then
        assertThatThrownBy(() -> userService.updatePassword(user.getId(), passwordDTO))
            .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void 회원_탈퇴() {
        //given
        SignUpUserDTO signUpUserDTO = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        signUpUserDTO.setEmail(email);
        signUpUserDTO.setNickname(nickname);
        signUpUserDTO.setPassword("12345");
        userService.signUp(signUpUserDTO);
        User user = userRepository.findByEmail(email);

        //when
        userService.deleteUser(user.getId());

        //then
        assertThat(userService.getUserInfo(user.getId())).isNull();
    }
}