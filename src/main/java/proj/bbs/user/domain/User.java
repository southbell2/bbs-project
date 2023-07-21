package proj.bbs.user.domain;

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
import proj.bbs.exception.BadRequestException;
import proj.bbs.exception.UnauthorizedException;
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
     *
     * @param newPassword : 새로 업데이트할 비밀번호
     * @param nowPassword : 유저가 본인 확인을 위해 보낸 현재 비밀번호
     * @param passwordEncoder : 비밀번호 해시를 위한 인코더
     *
     * 유저가 보낸 현재 비밀번호가 맞는지 검증 후에
     * 새로운 비밀번호를 해싱한 후 저장한다.
     */
    public void updatePassword(String newPassword, String nowPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.length() < 4 || 15 < newPassword.length()) {
            throw new BadRequestException("비밀번호가 적절하지 않습니다.");
        }

        if (!passwordEncoder.matches(nowPassword, this.password)) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        this.password = passwordEncoder.encode(newPassword);
    }

    private void setNickname(String nickname) {
        if (nickname == null || nickname.length() < 3 || 10 < nickname.length()) {
            throw new BadRequestException("닉네임이 적절하지 않습니다.");
        }
        this.nickname = nickname;
    }

}
