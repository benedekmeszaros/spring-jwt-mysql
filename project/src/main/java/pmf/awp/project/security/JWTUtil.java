package pmf.awp.project.security;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTUtil {

    @Value("${secret-key}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            secretKey = Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateToken(String email) {
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(email);
        Claims claims = claimsBuilder.build();
        Date now = new Date();
        Date validity = new Date(System.currentTimeMillis() + 1000 * 10 * 60);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

}