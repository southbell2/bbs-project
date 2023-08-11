package proj.bbs.constants;

import static proj.bbs.constants.Routes.*;

import java.util.Set;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String ACCESS_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_TYPE = "Bearer";
    public static final Set<String> UN_AUTHENTICATED_PATHS = Set.of(
        LOGIN.getPath(), SIGNUP.getPath(), REFRESH_TOKEN.getPath(),
            ADD_USER_ROLE.getPath(), DELETE_USER_ROLE.getPath());
}
