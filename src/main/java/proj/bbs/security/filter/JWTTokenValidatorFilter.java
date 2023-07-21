package proj.bbs.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import proj.bbs.config.UserPrincipal;
import proj.bbs.constants.SecurityConstants;
import proj.bbs.exception.InternalServerErrorException;

@Slf4j
@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);
        if (jwt != null) {
            SecretKey secretKey;
            try {
                secretKey = Keys.hmacShaKeyFor(
                    key.getBytes(StandardCharsets.UTF_8));
            } catch (InvalidKeyException e) {
                log.info("JWT Secret Key가 적절하지 않습니다");
                throw new InternalServerErrorException("Key error");
            }

            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

            Long id = Long.parseLong(claims.get("id").toString());
            String email = claims.getSubject();
            Authentication auth = new UsernamePasswordAuthenticationToken(
                new UserPrincipal(id, email), null,
                null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.ACCESS_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/login".equals(request.getRequestURI());
    }
}
