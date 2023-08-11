package proj.bbs.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "role ENUM('ADMIN', 'USER')")
    private RoleType role;

    public UserRole(RoleType role) {
        this.role = role;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
