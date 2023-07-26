package proj.bbs.constants;

import static org.springframework.http.HttpMethod.*;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum Routes {

    REFRESH_TOKEN("/refresh-token", GET),

    LOGIN("/login", POST),
    USERINFO("/userinfo", GET),
    UPDATE_USERINFO("/update-userinfo", PUT),
    UPDATE_PASSWORD("/update-password", PUT),
    DELETE_USER("/delete-user", DELETE),
    SIGNUP("/signup", POST),

    NEW_POST("/post", POST);

    private final String path;
    private final HttpMethod method;

    private Routes(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

}
