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

    ADD_USER_ROLE("/admin/add-user-role", POST),
    DELETE_USER_ROLE("/admin/delete-user-role", DELETE),
    USERINFO_ADMIN("/admin/userinfo/*", GET),
    DELETE_USER_ADMIN("/admin/delete-user", DELETE),
    SIGNUP_ADMIN("/admin/register-5f4dcc3b5aa765d61d8327deb882cf99", POST),
    USERINFO_LIST("/admin/userinfo-list", GET),

    NEW_POST("/post", POST),
    UPDATE_POST("/post/*", PUT),
    DELETE_POST("/post/*", DELETE),
    PAGED_POSTS("/post", GET),
    SHOW_POST("/post/*", GET);

    private final String path;
    private final HttpMethod method;

    private Routes(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

}
