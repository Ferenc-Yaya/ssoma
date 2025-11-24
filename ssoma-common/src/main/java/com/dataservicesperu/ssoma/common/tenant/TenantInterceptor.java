package com.dataservicesperu.ssoma.common.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    public static final String TENANT_HEADER = "X-Tenant-ID";
    public static final String EMPRESA_HEADER = "X-Empresa-ID";
    public static final String ES_HOST_HEADER = "X-Es-Host";
    public static final String ROL_HEADER = "X-Rol";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        String empresaId = request.getHeader(EMPRESA_HEADER);
        String esHostStr = request.getHeader(ES_HOST_HEADER);
        String codigoRol = request.getHeader(ROL_HEADER);

        if (tenantId != null && !tenantId.isBlank()) {
            TenantContext.setTenantId(tenantId.trim());
            log.debug("Tenant set from header: {}", tenantId);
        }

        if (empresaId != null && !empresaId.isBlank()) {
            TenantContext.setEmpresaId(empresaId.trim());
            log.debug("Empresa set from header: {}", empresaId);
        }

        if (esHostStr != null && !esHostStr.isBlank()) {
            TenantContext.setEsHost(Boolean.parseBoolean(esHostStr.trim()));
            log.debug("EsHost set from header: {}", esHostStr);
        }

        if (codigoRol != null && !codigoRol.isBlank()) {
            TenantContext.setCodigoRol(codigoRol.trim());
            log.debug("CodigoRol set from header: {}", codigoRol);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        TenantContext.clear();
    }
}
