package proj.bbs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.dto.UserInfoDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static proj.bbs.constants.Routes.*;
import static proj.bbs.constants.SecurityConstants.ACCESS_HEADER;
import static proj.bbs.constants.SecurityConstants.REFRESH_HEADER;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class SecurityTest {

    @Autowired
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Value("${admin.ip}")
    private String adminIP;

    String testEmail;
    String testNickname;
    String testPassword;

    @BeforeEach
    public void setUp() {
        SignUpUserDTO userDTO = new SignUpUserDTO();
        testEmail = "test@test.com";
        testNickname = "Terry";
        testPassword = "12345";
        userDTO.setEmail(testEmail);
        userDTO.setNickname(testNickname);
        userDTO.setPassword(testPassword);

        userService.signUp(userDTO);
    }

    @Test
    public void 로그인_성공() throws Exception {
        mockMvc.perform(post(LOGIN.getPath())
                        .with(httpBasic(testEmail, testPassword)))
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인_실패() throws Exception {
        //given
        String wrongPassword = "1234";

        //when && then
        mockMvc.perform(post(LOGIN.getPath())
                        .with(httpBasic(testEmail, wrongPassword)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 로그인_성공_후_유저정보얻기() throws Exception {
        //given
        MockHttpServletResponse loginResponse = mockMvc.perform(post(LOGIN.getPath())
                        .with(httpBasic(testEmail, testPassword)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String accessToken = loginResponse.getHeader(ACCESS_HEADER);
        Cookie refreshToken = loginResponse.getCookie(REFRESH_HEADER);

        //when
        MockHttpServletResponse userInfoResponse = mockMvc.perform(get(USERINFO.getPath())
                        .header(ACCESS_HEADER, accessToken)
                        .cookie(refreshToken))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        UserInfoDTO userInfoDTO = objectMapper.readValue(userInfoResponse.getContentAsString(), UserInfoDTO.class);

        //then
        assertThat(userInfoDTO.getEmail()).isEqualTo(testEmail);
        assertThat(userInfoDTO.getNickname()).isEqualTo(testNickname);
    }

    @Test
    public void 잘못된_Access_Token_보내기() throws Exception {
        //given
        MockHttpServletResponse loginResponse = mockMvc.perform(post(LOGIN.getPath())
                        .with(httpBasic(testEmail, testPassword)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String accessToken = loginResponse.getHeader(ACCESS_HEADER);
        Cookie refreshToken = loginResponse.getCookie(REFRESH_HEADER);
        String wrongAccessToken = accessToken + "a";

        //when && then
        mockMvc.perform(get(USERINFO.getPath())
                        .header(ACCESS_HEADER, wrongAccessToken)
                        .cookie(refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void Access_Token_재발급_받기() throws Exception {
        //given
        MockHttpServletResponse loginResponse = mockMvc.perform(post(LOGIN.getPath())
                        .with(httpBasic(testEmail, testPassword)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String accessToken = loginResponse.getHeader(ACCESS_HEADER);
        Cookie refreshToken = loginResponse.getCookie(REFRESH_HEADER);

        //when
        String reIssueAccessToken = mockMvc.perform(get(REFRESH_TOKEN.getPath())
                        .cookie(refreshToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader(ACCESS_HEADER);

        String responseBody = mockMvc.perform(get(USERINFO.getPath())
                        .header(ACCESS_HEADER, reIssueAccessToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserInfoDTO userInfoDTO = objectMapper.readValue(responseBody, UserInfoDTO.class);

        //then
        assertThat(userInfoDTO.getEmail()).isEqualTo(testEmail);
        assertThat(userInfoDTO.getNickname()).isEqualTo(testNickname);
    }

    @Test
    public void ADMIN_회원가입() throws Exception {
        //given
        SignUpUserDTO userDTO = new SignUpUserDTO();
        testEmail = "admin@test.com";
        testNickname = "admin";
        testPassword = "12345";
        userDTO.setEmail(testEmail);
        userDTO.setNickname(testNickname);
        userDTO.setPassword(testPassword);
        byte[] content = objectMapper.writeValueAsBytes(userDTO);

        MockHttpServletRequestBuilder request = post(SIGNUP_ADMIN.getPath())
                .with(new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setRemoteAddr(adminIP); // 원하는 IP 주소로 설정
                        request.setContentType("application/json");
                        request.setContent(content);

                        return request;
                    }
                });

        //when && then
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

}
