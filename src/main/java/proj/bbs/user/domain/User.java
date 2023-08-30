package proj.bbs.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import proj.bbs.exception.BadRequestException;
import proj.bbs.exception.UnauthorizedException;
import proj.bbs.post.domain.Post;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @BatchSize(size = 5)
    private List<UserRole> userRoles = new ArrayList<>();

    public static User createUser(SignUpUserDTO userDTO, PasswordEncoder passwordEncoder, UserRole... userRoles) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setPassword(userDTO.getPassword(), passwordEncoder);
        Arrays.stream(userRoles).forEach(user::addUserRole);

        return user;
    }

    public void updateUserInfo(UpdateUserInfoDTO userInfoDTO) {
        setNickname(userInfoDTO.getNickname());
    }

    /**
     * @param newPassword     : 새로 업데이트할 비밀번호
     * @param nowPassword     : 유저가 본인 확인을 위해 보낸 현재 비밀번호
     * @param passwordEncoder : 비밀번호 해시를 위한 인코더
     */
    public void updatePassword(String newPassword, String nowPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(nowPassword, this.password)) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        setPassword(newPassword, passwordEncoder);
    }

    public void addUserRole(UserRole userRole) {
        if (userRoles.stream().noneMatch(u -> u.getRole().equals(userRole.getRole()))) {
            userRoles.add(userRole);
            userRole.setUser(this);
        }
    }

    private void setPassword(String password, PasswordEncoder passwordEncoder) {
        if (password == null || password.length() < 4 || 15 < password.length()) {
            throw new BadRequestException("비밀번호가 적절하지 않습니다.");
        }
        this.password = passwordEncoder.encode(password);
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
