package proj.bbs.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.comment.service.dto.CommentResponse;
import proj.bbs.comment.service.dto.NewCommentDTO;
import proj.bbs.post.service.PostService;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
class CommentServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    String testEmail;
    String testNickname;
    String testPassword;
    long testPostId;

    @BeforeEach
    public void setUp() {
        //회원 생성
        SignUpUserDTO userDTO = new SignUpUserDTO();
        testEmail = "test@test.com";
        testNickname = "Terry";
        testPassword = "12345";
        userDTO.setEmail(testEmail);
        userDTO.setNickname(testNickname);
        userDTO.setPassword(testPassword);

        userService.signUp(userDTO);

        //게시글 생성
        NewPostDTO newPostDTO = new NewPostDTO();
        String title = "test title";
        String content = "test content";
        newPostDTO.setTitle(title);
        newPostDTO.setContent(content);
        testPostId = postService.savePost(newPostDTO, testEmail);
    }

    @Test
    public void 댓글_등록() {
        //given
        NewCommentDTO newCommentDTO = new NewCommentDTO();
        String testComment1 = "test comment1";
        newCommentDTO.setContent(testComment1);
        commentService.saveComment(newCommentDTO, testEmail, testPostId);
        String testComment2 = "test comment2";
        newCommentDTO.setContent(testComment2);
        commentService.saveComment(newCommentDTO, testEmail, testPostId);

        //when
        Page<CommentResponse> page = commentService.getCommentPage(0, 5, testPostId);

        //then
        List<CommentResponse> content = page.getContent();
        assertThat(content.get(0).getContent()).isEqualTo(testComment1);
        assertThat(content.get(1).getContent()).isEqualTo(testComment2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}