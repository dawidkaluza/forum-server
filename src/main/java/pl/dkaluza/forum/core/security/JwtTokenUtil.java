package pl.dkaluza.forum.core.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
class JwtTokenUtil {
    private final String jwtSecret;
    private final Integer jwtExpirationDays;

    @Autowired
    public JwtTokenUtil(Environment environment) {
        jwtSecret = environment.getProperty("jwt.secret", "notSoSecret");
        jwtExpirationDays = environment.getProperty("jwt.expirationDays", Integer.class, 7);
    }

    public String generateToken(long userId, String username) {
        return Jwts.builder()
            //TODO move "userId" to claims
            .setSubject(userId + "," + username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationDays * 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }

    public long getUserId(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return Long.parseLong(
            claims.getSubject().split(",")[0]
        );
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
