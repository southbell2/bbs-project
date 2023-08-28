package proj.bbs.post.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import proj.bbs.post.domain.Post;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void savePost(Post post) {
        em.persist(post);
    }

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    public void deletePost(Long postId) {
        em.createQuery("DELETE FROM Post p WHERE p.id = :id")
                .setParameter("id", postId)
                .executeUpdate();
    }
}
