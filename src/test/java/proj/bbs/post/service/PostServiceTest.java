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
import proj.bbs.post.service.dto.PagedPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;

import java.util.List;

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
    UserRepository userRepository;
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

    @Test
    public void 내_글_보기() {
        //given
        NewPostDTO newPostDTO = new NewPostDTO();
        String title1 = "test title 1";
        String content1 = "test content 1";
        newPostDTO.setTitle(title1);
        newPostDTO.setContent(content1);
        long post1 = postService.savePost(newPostDTO, testEmail);

        String title2 = "test title 2";
        String content2 = "test content 2";
        newPostDTO.setTitle(title2);
        newPostDTO.setContent(content2);
        long post2 = postService.savePost(newPostDTO, testEmail);

        String title3 = "test title 3";
        String content3 = "test content 3";
        newPostDTO.setTitle(title3);
        newPostDTO.setContent(content3);
        long post3 = postService.savePost(newPostDTO, testEmail);

        User writer = userRepository.findByEmail(testEmail);

        //when
        List<PagedPostDTO> page1 = postService.getPagedUserPosts(writer.getId(), 0, 2);
        List<PagedPostDTO> page2 = postService.getPagedUserPosts(writer.getId(), 2, 2);

        //then
        assertThat(page1.get(0).getId()).isEqualTo(post3);
        assertThat(page1.get(1).getId()).isEqualTo(post2);

        assertThat(page2.size()).isEqualTo(1);
        assertThat(page2.get(0).getId()).isEqualTo(post1);
    }
}