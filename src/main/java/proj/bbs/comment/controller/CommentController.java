package proj.bbs.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import proj.bbs.comment.service.CommentService;
import proj.bbs.comment.service.dto.CommentResponse;
import proj.bbs.comment.service.dto.NewCommentDTO;
import proj.bbs.security.principal.UserPrincipal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Void> createComment(@RequestParam Long postId, @RequestBody NewCommentDTO newCommentDTO,
                                              Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        commentService.saveComment(newCommentDTO, userPrincipal.getEmail(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment/{postId}")
    public ResponseEntity<Page<CommentResponse>> viewComments(@RequestParam Integer page, @RequestParam Integer size,
                                                              @PathVariable Long postId) {
        Page<CommentResponse> commentResponsePage = commentService.getCommentPage(page, size, postId);
        return ResponseEntity.ok(commentResponsePage);
    }
}
