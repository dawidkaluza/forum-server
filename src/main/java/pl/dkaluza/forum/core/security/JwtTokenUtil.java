package pl.dkaluza.forum.core.security;

import io.jsonwebtoken.*;

import java.util.Date;


class JwtTokenUtil {
    private final String jwtSecret = "jBL4Y2URv5hC3Vz9cc4ta2pfe";
    private final String jwtIssuer = "dkaluza.pl";

    public String generateToken(long userId, String username) {
        return Jwts.builder()
            .setSubject(userId + "," + username)
            .setIssuer(jwtIssuer)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
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
