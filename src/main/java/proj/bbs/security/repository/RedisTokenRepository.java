package proj.bbs.security.repository;

import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import proj.bbs.security.entity.RefreshToken;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository{

    private final RedisCommands<String, String> sync;
    private final RedisAsyncCommands<String, String> async;

    @Override
    public void saveToken(RefreshToken refreshToken) {
        sync.hmset(getTokenKey(refreshToken.getToken()), refreshTokenToMap(refreshToken));
    }

    @Override
    public Long getTokenExpiration(String token) {
        return Long.parseLong(sync.hget(getTokenKey(token), "exp"));
    }

    @Override
    public void deleteToken(String token) {
        sync.del(getTokenKey(token));
    }

    private String getTokenKey(String token) {
        return "token:" + token;
    }

    private Map<String, String> refreshTokenToMap(RefreshToken token) {
        return Map.of("userId", String.valueOf(token.getUserId()),
            "email", token.getEmail(),
            "exp", String.valueOf(token.getExp()));
    }
}
