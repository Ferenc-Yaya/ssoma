package com.dataservicesperu.ssoma.auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un JWT con tenant_id como claim
     */
    public String generateToken(String username, String tenantId, UUID empresaId, Boolean esHost, String codigoRol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenant_id", tenantId);
        claims.put("empresa_id", empresaId != null ? empresaId.toString() : null);
        claims.put("es_host", esHost);
        claims.put("rol", codigoRol);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Agregar método para extraer la info de superadmin
    public Boolean extractIsSuperAdmin(String token) {
        Claims claims = validateToken(token);
        return claims.get("is_super_admin", Boolean.class);
    }

    public String extractRole(String token) {
        Claims claims = validateToken(token);
        return claims.get("role", String.class);
    }
    /**
     * Valida el token y retorna los claims
     */
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el tenant_id del token
     */
    public String extractTenantId(String token) {
        Claims claims = validateToken(token);
        return claims.get("tenant_id", String.class);
    }

    /**
     * Extrae el nombre de usuario (subject) del token
     */
    public String extractNombreUsuario(String token) {
        return validateToken(token).getSubject();
    }
}
