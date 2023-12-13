package br.com.tech.challenge.servicos;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiration;

    @Value("${security.jwt.chave-assinatura}")
    private String signatureKey;

    private SecretKey getSignatureKey() {
        byte[] decodedKey = Base64.getDecoder().decode(signatureKey);
        return new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(UserDetails userDetails) {
        long expirationInMinutes = Long.parseLong(expiration);
        LocalDateTime dateTimeExpiration = LocalDateTime.now().plusMinutes(expirationInMinutes);
        Instant instant = dateTimeExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(instant);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expirationDate)
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();
            LocalDateTime expirationLocalDateTime = expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return LocalDateTime.now().isBefore(expirationLocalDateTime);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

}