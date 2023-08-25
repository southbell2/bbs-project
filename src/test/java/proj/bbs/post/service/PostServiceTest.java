package proj.bbs.post.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;

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
    public void 게시글_등록() {
        //given
        NewPostDTO newPostDTO = new NewPostDTO();
        String title = "test title";
        String content = "test content";
        newPostDTO.setTitle(title);
        newPostDTO.setContent(content);
        String email = "test@test.com";

        //when
        long id = postService.savePost(newPostDTO, email);
        em.flush();
        em.clear();

        //then
        PostDTO postDTO = postService.getPost(id);
        assertThat(postDTO.getContent()).isEqualTo(content);
        assertThat(postDTO.getId()).isEqualTo(id);
        assertThat(postDTO.getNickname()).isEqualTo(testNickname);
    }
}