package proj.bbs.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshToken {
    private String token;
    private Long userId;
    private String email;
    private Long exp;
}
