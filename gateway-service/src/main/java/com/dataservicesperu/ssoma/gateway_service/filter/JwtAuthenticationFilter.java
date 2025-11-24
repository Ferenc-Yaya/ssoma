package com.dataservicesperu.ssoma.gateway_service.filter;

import com.dataservicesperu.ssoma.gateway_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // Rutas públicas que no requieren autenticación
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/health"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Permitir rutas públicas sin token
        if (isPublicPath(path)) {
            log.debug("Public path accessed: {}", path);
            return chain.filter(exchange);
        }

        // Obtener token del header Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                log.warn("Invalid or expired token for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Extraer claims del token
            String username = jwtUtil.extractUsername(token);
            String tenantId = jwtUtil.extractTenantId(token);
            String empresaId = jwtUtil.extractEmpresaId(token);
            Boolean esHost = jwtUtil.extractEsHost(token);
            String role = jwtUtil.extractRole(token);

            log.debug("Token valid for user: {}, tenant: {}, role: {}", username, tenantId, role);

            // Agregar headers para los microservicios
            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .header("X-Tenant-ID", tenantId != null ? tenantId : "")
                    .header("X-Es-Host", String.valueOf(esHost != null && esHost))
                    .header("X-Rol", role != null ? role : "");

            if (empresaId != null) {
                requestBuilder.header("X-Empresa-ID", empresaId);
            }

            ServerHttpRequest modifiedRequest = requestBuilder.build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            log.error("Error processing JWT: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return -100; // Alta prioridad
    }
}
