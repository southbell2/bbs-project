package proj.bbs.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import proj.bbs.post.domain.Post;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

@Entity
@Table(name = "users")
@Getter
@DynamicInsert
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL UNIQUE")
    private String email;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String password;

    @Column(columnDefinition = "CHAR(10) NOT NULL UNIQUE")
    private String nickname;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Post> myPosts = new ArrayList<>();

    public void updateUserInfo(UpdateUserInfoDTO userInfoDTO) {
        setNickname(userInfoDTO.getNickname());
    }

    /**
     * 비밀번호를 해싱후 셋팅해야 한다.
     */
    public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(newPassword));
    }

    private void setNickname(String nickname) {
        Objects.requireNonNull(nickname, "닉네임을 입력하세요");
        if (nickname.length() < 3 || 10 < nickname.length()) {
            throw new IllegalArgumentException("닉네임의 크기가 적절하지 않습니다");
        }
        this.nickname = nickname;
    }

    private void setPassword(String password) {
        Objects.requireNonNull(password, "비밀번호를 입력하세요");
        this.password = password;
    }

}
