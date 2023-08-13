package proj.bbs.user.service.dto;

import lombok.Getter;
import lombok.Setter;
import proj.bbs.user.domain.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserInfoAdminDTO {

    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    private List<UserRole> roles;
}
