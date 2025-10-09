package com.dataservicesperu.ssoma.roles_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String tenantId = req.getHeader("X-Tenant-ID");

        if (tenantId != null) {
            CurrentTenantHolder.setTenantId(tenantId);
        } else {
            // Esquema por defecto para pruebas internas
            CurrentTenantHolder.setTenantId("public");
        }

        try {
            chain.doFilter(request, response);
        } finally {
            CurrentTenantHolder.clear();
        }
    }
}
