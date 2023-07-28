package proj.bbs.security.repository;

import java.util.Map;
import proj.bbs.security.entity.RefreshToken;

public interface TokenRepository {

    void saveToken(RefreshToken refreshToken);

    Long getTokenExpiration(String token);

    Map<String, String> getTokenInfo(String token);

    void deleteToken(String token);

    boolean isTokenPresent(String token);
}
