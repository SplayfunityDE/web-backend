package de.splayfer.web_backend.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final String secretKey = System.getenv("JWT_KEY"); // Secret for signing JWTs
    private final long shortExpirationMs = 10800000; // Token validity (3 hour)
    private final long longExpirationMs = 86400000; // Token validity (24 hours)

    public String generateToken(String username, boolean remember) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (remember ? longExpirationMs : shortExpirationMs)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact(); // Creates the token
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // Verifies token integrity
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Extracts the username
    }
}
