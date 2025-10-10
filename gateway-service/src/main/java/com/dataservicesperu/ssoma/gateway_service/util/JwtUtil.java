package com.dataservicesperu.ssoma.gateway_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Valida el token y retorna los claims
     */
    public Claims validateAndExtractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido: " + e.getMessage());
        }
    }

    /**
     * Extrae el tenant_id del token
     */
    public String extractTenantId(String token) {
        Claims claims = validateAndExtractClaims(token);
        return claims.get("tenant_id", String.class);
    }

    /**
     * Extrae el nombre de usuario (subject) del token
     */
    public String extractNombreUsuario(String token) {  // ✅ CAMBIADO
        return validateAndExtractClaims(token).getSubject();
    }

    /**
     * Extrae el role del token
     */
    public String extractRole(String token) {
        Claims claims = validateAndExtractClaims(token);
        return claims.get("role", String.class);
    }
}
