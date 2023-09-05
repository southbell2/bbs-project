package proj.bbs.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import proj.bbs.comment.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Page<Comment> findByPost_Id(long postId, Pageable pageable);
}
