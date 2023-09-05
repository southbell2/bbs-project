package proj.bbs.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.IdGenerator;
import proj.bbs.comment.domain.Comment;
import proj.bbs.comment.repository.CommentRepository;
import proj.bbs.comment.service.dto.CommentResponse;
import proj.bbs.comment.service.dto.NewCommentDTO;
import proj.bbs.post.domain.Post;
import proj.bbs.post.repository.PostRepository;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public long saveComment(NewCommentDTO newCommentDTO, String email, long postId) {
        long id = IdGenerator.generate();
        Post post = postRepository.findById(postId);
        User writer = userRepository.findByEmail(email);
        Comment comment = Comment.createComment(id, newCommentDTO.getContent(), post, writer);
        commentRepository.saveComment(comment);
        return id;
    }

    public Page<CommentResponse> getCommentPage(int page, int size, long postId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "Post_Id"));
        Page<Comment> commentPage = commentRepository.findByPost_Id(postId, pageRequest);
        return commentPage.map(comment -> new CommentResponse(comment.getContent(), comment.getNickname()));

    }
}
