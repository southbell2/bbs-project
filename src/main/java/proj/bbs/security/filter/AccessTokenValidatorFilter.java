package proj.bbs.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import proj.bbs.exception.AccessTokenExpiredException;
import proj.bbs.security.AccessTokenManager;
import proj.bbs.security.TokenStatus;

import java.io.IOException;

import static proj.bbs.constants.SecurityConstants.ACCESS_HEADER;
import static proj.bbs.constants.SecurityConstants.UN_AUTHENTICATED_PATHS;
import static proj.bbs.security.TokenStatus.EXPIRED;
import static proj.bbs.security.TokenStatus.OK;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenValidatorFilter extends OncePerRequestFilter {

    private final AccessTokenManager accessTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);
        TokenStatus accessTokenStatus = accessTokenManager.validateAccessToken(accessToken);
        if (accessTokenStatus == OK) {
            Authentication authentication = accessTokenManager.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (accessTokenStatus == EXPIRED) {
            throw new AccessTokenExpiredException("Access Token Expired");
        } else {
            throw new BadCredentialsException("Invalid Token");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //인증이 필요하지 않은 경로에서는 토큰을 검증하지 않는다.
        return UN_AUTHENTICATED_PATHS.contains(request.getRequestURI());
    }
}
