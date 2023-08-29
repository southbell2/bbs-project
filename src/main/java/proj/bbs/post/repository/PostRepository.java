package proj.bbs.post.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import proj.bbs.post.domain.Post;
import proj.bbs.user.domain.User;

import java.util.List;

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

    public List<Post> findPagedPosts(Long beforeId, Integer limit) {
        return em.createQuery(
                        "SELECT p FROM Post p " +
                                "WHERE p.id < :id " +
                                "ORDER BY p.id DESC", Post.class)
                .setFirstResult(0)
                .setMaxResults(limit)
                .setParameter("id", beforeId)
                .getResultList();
    }
}
