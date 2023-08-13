package proj.bbs.user.service.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private String email;
    private String nickname;
    private LocalDateTime createdAt;

}
