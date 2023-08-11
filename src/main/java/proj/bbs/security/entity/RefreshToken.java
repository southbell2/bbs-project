package proj.bbs.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class RefreshToken {
    private String token;
    private Long userId;
    private String email;
    private Long exp;
    private Set<String> authorities;
}
