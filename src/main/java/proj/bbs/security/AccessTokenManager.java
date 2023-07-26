package proj.bbs.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import proj.bbs.security.principal.UserPrincipal;

@Component
@Slf4j
public class AccessTokenManager {

    private final String key;
    private final Long accessTokenValidityInMs;
    private SecretKey secretKey;

    @Autowired
    public AccessTokenManager(@Value("${jwt.secret-key}") String key,
        @Value("${jwt.token-validity-in-sec}") Long accessTokenValidity) {
        this.key = key;
        this.accessTokenValidityInMs = accessTokenValidity * 1000L;
    }

    @PostConstruct
    public void postConstruct() {
        secretKey = Keys.hmacShaKeyFor(
            key.getBytes(StandardCharsets.UTF_8));
    }

    public TokenStatus validateAccessToken(String jwt) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt);
            return TokenStatus.OK;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("JWT 검증 도중 예외 발생 e = {}", e);
            return TokenStatus.DENIED;
        }
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(jwt)
            .getBody();

        Long id = Long.parseLong(claims.get("id").toString());
        String email = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(
            new UserPrincipal(id, email), null,
            null);
    }

    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder().setSubject(userPrincipal.getEmail())
            .claim("id", userPrincipal.getId())
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(secretKey).compact();
    }

}
