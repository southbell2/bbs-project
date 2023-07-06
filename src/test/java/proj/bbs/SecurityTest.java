package proj.bbs;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;

    @Test
    public void 인증되지않은_사용자() throws Exception {
        mvc.perform(get("/security_test"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 인증된_사용자() throws Exception {
        mvc.perform(get("/security_test"))
            .andExpect(status().isOk())
            .andExpect(content().string("security test"));
    }

    @Test
    @Transactional
    public void HttpBasic_인증() throws Exception {
        //Given
        SignUpUserDTO user = new SignUpUserDTO();
        user.setEmail("test@test.com");
        user.setNickname("Terry");
        user.setPassword("12345");
        userService.signUp(user);

        //When && Then
        //인증 성공
        mvc.perform(
                post("/login")
                    .with(httpBasic("test@test.com", "12345")))
            .andExpect(status().isOk());

        //인증 실패
        mvc.perform(
                post("/login")
                    .with(httpBasic("test@test.com", "123456")))
            .andExpect(status().isUnauthorized());
    }

}
