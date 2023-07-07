package proj.bbs.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
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

    public void updateUserInfo(UpdateUserInfoDTO userInfoDTO) {
        setNickname(userInfoDTO.getNickname());
    }

    private void setNickname(String nickname) {
        Objects.requireNonNull(nickname, "닉네임을 입력하세요");
        if (nickname.length() < 3 || 10 < nickname.length()) {
            throw new IllegalArgumentException("닉네임의 크기가 적절하지 않습니다");
        }
        this.nickname = nickname;
    }

}
