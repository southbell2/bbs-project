package proj.bbs.security.filter;

import static proj.bbs.constants.Routes.REFRESH_TOKEN;
import static proj.bbs.constants.SecurityConstants.REFRESH_HEADER;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import proj.bbs.exception.UnauthorizedException;
import proj.bbs.security.RefreshTokenManager;
import proj.bbs.security.TokenStatus;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenValidatorFilter extends OncePerRequestFilter {

    private final RefreshTokenManager refreshTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String refreshToken = getRefTokenFromCookie(request);
        TokenStatus tokenStatus = refreshTokenManager.validateRefreshToken(refreshToken);

        if (tokenStatus != TokenStatus.OK) {
            log.info("TokenStatus = {}", tokenStatus);
            throw new UnauthorizedException("다시 로그인을 해주세요.");
        }

        Authentication authentication = refreshTokenManager.getAuthentication(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getRefTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refToken = null;
        if (cookies != null) {
            refToken = Arrays.stream(cookies)
                .filter(cookie -> REFRESH_HEADER.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        }
        return refToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !REFRESH_TOKEN.getPath().equals(request.getRequestURI());
    }
}
