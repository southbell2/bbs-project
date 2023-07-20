package proj.bbs.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPrincipal {

    private final Long id;
    private final String email;

}
