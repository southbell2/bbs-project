package proj.bbs.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import proj.bbs.post.service.PostService;
import proj.bbs.post.service.dto.NewPostDTO;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(NewPostDTO newPostDTO, Authentication authentication) {
        postService.savePost(newPostDTO, authentication.getName());
        return ResponseEntity.ok().build();
    }

}
