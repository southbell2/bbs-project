package proj.bbs.user.repository;

import java.util.List;

import proj.bbs.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import proj.bbs.user.domain.UserRole;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void saveUser(User user) {
        em.persist(user);
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByIdWithRole(Long id) {
        return em.createQuery(
                        "SELECT u FROM User u " +
                                "JOIN FETCH u.roles r " +
                                "WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public User findByEmail(String email) {
        return em.createQuery(
                        "SELECT u " +
                                "FROM User u " +
                                "WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User findByEmailWithRole(String email) {
        return em.createQuery(
                        "SELECT u FROM User u " +
                                "JOIN FETCH u.roles r " +
                                "WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void deleteUser(User user) {
        em.remove(user);
    }

    public void saveUserRole(UserRole userRole) {
        em.persist(userRole);
    }

    public void deleteUserRole(UserRole userRole) {
        em.createNativeQuery("DELETE FROM user_role WHERE id = :id")
                .setParameter("id", userRole.getId())
                .executeUpdate();
    }

}
