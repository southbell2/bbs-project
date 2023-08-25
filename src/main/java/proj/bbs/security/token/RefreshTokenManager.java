package proj.bbs.security.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import proj.bbs.constants.Routes;
import proj.bbs.constants.SecurityConstants;
import proj.bbs.security.entity.RefreshToken;
import proj.bbs.security.principal.UserPrincipal;
import proj.bbs.security.repository.TokenRepository;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

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
            userPrincipal.getEmail(), exp, authorityListToSet(authentication.getAuthorities())));
        log.info("new token created for user, userId = {}", userPrincipal.getId());

        return token;
    }

    public String reIssueRefreshToken(String oldRefToken, Authentication authentication) {
        String newRefToken = UUID.randomUUID().toString();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long exp = tokenRepository.getTokenExpiration(oldRefToken);

        tokenRepository.deleteToken(oldRefToken);

        tokenRepository.saveToken(new RefreshToken(newRefToken, userPrincipal.getId(),
            userPrincipal.getEmail(), exp, authorityListToSet(authentication.getAuthorities())));

        return newRefToken;
    }

    public TokenStatus validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            return TokenStatus.DENIED;
        }

        boolean tokenPresent = tokenRepository.isTokenPresent(refreshToken);
        if (!tokenPresent) {
            return TokenStatus.DENIED;
        }

        long now = new Date().getTime();
        Long exp = tokenRepository.getTokenExpiration(refreshToken);
        if (exp <= now) {
            return TokenStatus.EXPIRED;
        }

        return TokenStatus.OK;
    }

    public Authentication getAuthentication(String refreshToken) {
        Map<String, String> tokenInfoMap = tokenRepository.getTokenInfo(refreshToken);
        long id = Long.parseLong(tokenInfoMap.get("id"));
        String email = tokenInfoMap.get("email");
        String authorities = tokenInfoMap.get("authorities");

        UserPrincipal userPrincipal = new UserPrincipal(id, email);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null,
            commaSeparatedStringToAuthorityList(authorities));
    }

    public void addRefTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(SecurityConstants.REFRESH_HEADER, refreshToken);
        cookie.setMaxAge((int) (refreshTokenValidityInMs / 1000));
        cookie.setPath(Routes.REFRESH_TOKEN.getPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
