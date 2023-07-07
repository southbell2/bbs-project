package proj.bbs.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.exception.NotFoundException;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원_가입_성공() {
        //Given
        SignUpUserDTO user = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        user.setEmail(email);
        user.setNickname(nickname);
        user.setPassword("12345");

        //When
        userService.signUp(user);
        List<User> userList = userRepository.findByEmail(email);

        //Then
        assertThat(userList.get(0).getEmail()).isEqualTo(email);
        assertThat(userList.get(0).getNickname()).isEqualTo(nickname);
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
        SignUpUserDTO user = new SignUpUserDTO();
        String email = "test@test.com";
        String nickname = "Terry";
        user.setEmail(email);
        user.setNickname(nickname);
        user.setPassword("12345");
        userService.signUp(user);

        //when
        UserInfoDTO userInfo = userService.getUserInfo(email);

        //then
        assertThat(userInfo.getEmail()).isEqualTo(email);
        assertThat(userInfo.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void 회원_정보_얻기_실패() {
        //given
        String email = "test@test.com";

        //when && then
        assertThatThrownBy(() -> userService.getUserInfo(email))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("요청한 회원을 찾을 수 없습니다");

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

        nickname = "Spring";
        UpdateUserInfoDTO userInfoDTO = new UpdateUserInfoDTO();
        userInfoDTO.setEmail(email);
        userInfoDTO.setNickname(nickname);

        //when
        userService.updateUserInfo(userInfoDTO);

        //then
        List<User> user = userRepository.findByEmail(email);
        assertThat(user.get(0).getNickname()).isEqualTo(nickname);

    }

}