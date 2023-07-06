package proj.bbs.user.repository;

import java.util.List;
import proj.bbs.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void saveUser(User user) {
        em.persist(user);
    }

    public List<User> findByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email",
                User.class)
            .setParameter("email", email)
            .getResultList();
    }

}
