package proj.bbs.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import proj.bbs.post.service.PostService;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PostDTO;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Void> createPost(NewPostDTO newPostDTO, Authentication authentication) {
        long postId = postService.savePost(newPostDTO, authentication.getName());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{number}")
                .buildAndExpand(postId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDTO> showPost(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPost(postId);
        return ResponseEntity.ok(postDTO);
    }

}
