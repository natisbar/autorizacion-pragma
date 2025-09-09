package co.com.pragma.api.seguridad.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secretkey}")
    private String secretKey;
    @Value("${security.jwt.expiration}")
    private long expirationMs;

    public String generarToken(Long idUsuario, BigDecimal salarioBase, String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", idUsuario)
                .claim("salarioBase", salarioBase)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

