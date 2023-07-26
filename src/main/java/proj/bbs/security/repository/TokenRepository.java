package proj.bbs.security.repository;

import proj.bbs.security.entity.RefreshToken;

public interface TokenRepository {

    void saveToken(RefreshToken refreshToken);

    Long getTokenExpiration(String token);

    void deleteToken(String token);
}
