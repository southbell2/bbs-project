package proj.bbs.security.filter;

import static proj.bbs.constants.Routes.*;
import static proj.bbs.constants.SecurityConstants.REFRESH_HEADER;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import proj.bbs.constants.SecurityConstants;
import proj.bbs.security.token.AccessTokenManager;
import proj.bbs.security.token.RefreshTokenManager;

@Component
@RequiredArgsConstructor
public class TokenGeneratorFilter extends OncePerRequestFilter {

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadCredentialsException("회원 정보가 존재하지 않습니다.");
        }

        String accessToken = accessTokenManager.createAccessToken(authentication);
        response.setHeader(SecurityConstants.ACCESS_HEADER,
            SecurityConstants.BEARER_TYPE + " " + accessToken);

        String oldRefToken = getRefTokenFromCookie(request);
        String newRefToken;
        if (oldRefToken == null) {
            newRefToken = refreshTokenManager.createRefreshToken(authentication);
        } else {
            newRefToken = refreshTokenManager.reIssueRefreshToken(oldRefToken, authentication);
        }
        refreshTokenManager.addRefTokenToCookie(response, newRefToken);

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
        String uri = request.getRequestURI();
        return !(LOGIN.getPath().equals(uri) || REFRESH_TOKEN.getPath().equals(uri));
    }
}
