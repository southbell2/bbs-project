package proj.bbs.comment.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import proj.bbs.comment.domain.Comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final EntityManager em;

    @Override
    public void saveComment(Comment comment) {
        em.persist(comment);
    }
}
