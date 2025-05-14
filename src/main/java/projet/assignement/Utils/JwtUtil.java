package projet.assignement.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import projet.assignement.Entities.User;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import io.github.cdimascio.dotenv.Dotenv;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JwtUtil() {
        Dotenv dotenv = Dotenv.configure().load();
        this.secretKey = new SecretKeySpec(
                dotenv.get("JWT_SECRET").getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
        this.jwtExpiration = Long.parseLong(dotenv.get("JWT_EXPIRATION"));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(user.getId())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}