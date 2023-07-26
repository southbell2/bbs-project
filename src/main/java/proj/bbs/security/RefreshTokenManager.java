package proj.bbs.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import proj.bbs.constants.Routes;
import proj.bbs.constants.SecurityConstants;
import proj.bbs.security.entity.RefreshToken;
import proj.bbs.security.principal.UserPrincipal;
import proj.bbs.security.repository.TokenRepository;

@Component
@Slf4j
public class RefreshTokenManager {

    private final Long refreshTokenValidityInMs;
    private final TokenRepository tokenRepository;

    @Autowired
    public RefreshTokenManager(
        @Value("${jwt.refresh-token-validity-in-sec}") Long refreshTokenValidity,
        TokenRepository tokenRepository) {
        this.refreshTokenValidityInMs = refreshTokenValidity * 1000L;
        this.tokenRepository = tokenRepository;
    }

    public String createRefreshToken(Authentication authentication) {
        String token = UUID.randomUUID().toString();
        Long exp = new Date().getTime() + refreshTokenValidityInMs;
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        tokenRepository.saveToken(new RefreshToken(token, userPrincipal.getId(),
            userPrincipal.getEmail(), exp));

        return token;
    }

    public String reIssueRefreshToken(String oldRefToken, Authentication authentication) {
        String newRefToken = UUID.randomUUID().toString();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long exp = tokenRepository.getTokenExpiration(oldRefToken);

        tokenRepository.deleteToken(oldRefToken);

        tokenRepository.saveToken(new RefreshToken(newRefToken, userPrincipal.getId(),
            userPrincipal.getEmail(), exp));

        return newRefToken;
    }

    public TokenStatus validateRefreshToken(String ref) {
        return TokenStatus.OK;
    }

    public Authentication getAuthentication(String ref) {
        return new UsernamePasswordAuthenticationToken(
            new UserPrincipal(17L, "user@test"), null,
            null);
    }

    public void addRefTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(SecurityConstants.REFRESH_HEADER, refreshToken);
        cookie.setMaxAge((int) (refreshTokenValidityInMs / 1000));
        cookie.setPath(Routes.REFRESH_TOKEN.getPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
