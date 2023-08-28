package proj.bbs.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import proj.bbs.exception.ForbiddenException;
import proj.bbs.post.domain.Post;
import proj.bbs.post.repository.PostRepository;
import proj.bbs.post.service.PostService;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.post.service.dto.UpdatePostDTO;
import proj.bbs.security.principal.UserPrincipal;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity<Void> createPost(@RequestBody NewPostDTO newPostDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        long postId = postService.savePost(newPostDTO, userPrincipal.getEmail());
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

    @PutMapping("/post/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody UpdatePostDTO updatePostDTO, Authentication authentication) {
        Post post = postRepository.findById(postId);
        Long postUserId = post.getUser().getId();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long loginUserId = userPrincipal.getId();

        if (!isPostWriter(postUserId, loginUserId)) {
            throw new ForbiddenException("글 쓴 사람만 수정할 수 있습니다.");
        }

        postService.updatePost(updatePostDTO, postId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        Post post = postRepository.findById(postId);
        Long postUserId = post.getUser().getId();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long loginUserId = userPrincipal.getId();

        if (!isPostWriter(postUserId, loginUserId)) {
            throw new ForbiddenException("글 쓴 사람만 삭제할 수 있습니다.");
        }

        postService.deletePost(postId);

        return ResponseEntity.ok().build();
    }

    private boolean isPostWriter(Long postUserId, Long loginUserId) {
        if (postUserId == null || loginUserId == null) {
            return false;
        }
        return postUserId.equals(loginUserId);
    }

}
