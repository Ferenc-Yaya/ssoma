package com.dataservicesperu.ssoma.common.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    public static final String TENANT_HEADER = "X-Tenant-ID";
    public static final String EMPRESA_HEADER = "X-Empresa-ID";
    public static final String IS_HOST_HEADER = "X-Es-Host";

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        String tenantId = request.getHeader(TENANT_HEADER);
        String empresaId = request.getHeader(EMPRESA_HEADER);
        String isHost = request.getHeader(IS_HOST_HEADER);

        if (tenantId != null && !tenantId.isBlank()) {
            TenantContext.setTenantId(tenantId.toUpperCase().trim());
        } else {
            log.warn("No tenant header found in request: {} {}", 
                    request.getMethod(), request.getRequestURI());
        }

        if (empresaId != null && !empresaId.isBlank()) {
            TenantContext.setEmpresaContratista(UUID.fromString(empresaId.trim()));
        }

        TenantContext.setEmpresaHost("true".equalsIgnoreCase(isHost));

        return true;
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex) {
        TenantContext.clear();
    }
}
