package com.dataservicesperu.ssoma.gateway_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractTenantId(String token) {
        return (String) extractAllClaims(token).get("tenant_id");
    }

    public String extractEmpresaId(String token) {
        return (String) extractAllClaims(token).get("empresa_id");
    }

    public Boolean extractEsHost(String token) {
        return (Boolean) extractAllClaims(token).get("es_host");
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("rol");
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().getTime() > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }
}
