package com.dataservicesperu.ssoma.gateway_service.filter;

import com.dataservicesperu.ssoma.gateway_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Rutas públicas (sin autenticación)
        if (path.startsWith("/api/auth/") ||
                path.endsWith(".html") ||
                path.equals("/")) {
            return chain.filter(exchange);
        }

        // Resto del código de validación JWT...
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);
            String tenantId = jwtUtil.extractTenantId(token);
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            System.out.println("=== GATEWAY - REQUEST INTERCEPTADO ===");
            System.out.println("Path: " + path);
            System.out.println("Email: " + email);
            System.out.println("Tenant ID: " + tenantId);
            System.out.println("Role: " + role);

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(builder -> builder.header("X-Tenant-ID", tenantId))
                    .build();

            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            System.err.println("❌ ERROR validando JWT: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
