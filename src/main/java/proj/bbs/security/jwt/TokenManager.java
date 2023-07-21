package proj.bbs.security.jwt;

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
import proj.bbs.config.UserPrincipal;
import proj.bbs.security.TokenStatus;

@Component
@Slf4j
public class TokenManager {

    private final String key;
    private final Long accessTokenValidityInMs;
    private final Long refreshTokenValidityInMs;
    private SecretKey secretKey;

    @Autowired
    public TokenManager(@Value("${jwt.secret-key}") String key,
        @Value("${jwt.token-validity-in-sec}") Long accessTokenValidity,
        @Value("${jwt.refresh-token-validity-in-sec}") Long refreshTokenValidity) {
        this.key = key;
        this.accessTokenValidityInMs = accessTokenValidity * 1000L;
        this.refreshTokenValidityInMs = refreshTokenValidity * 1000L;
    }

    @PostConstruct
    public void postConstruct() {
        secretKey = Keys.hmacShaKeyFor(
            key.getBytes(StandardCharsets.UTF_8));
    }

    public TokenStatus validateToken(String jwt) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt);
            return TokenStatus.ACCESS;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("JwtException : {}", e);
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
