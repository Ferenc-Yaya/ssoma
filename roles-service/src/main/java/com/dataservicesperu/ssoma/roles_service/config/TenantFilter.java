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
        String isSuperAdmin = req.getHeader("X-Super-Admin");

        // Si es super admin, establecemos el flag pero mantenemos el tenant
        if ("true".equals(isSuperAdmin)) {
            CurrentTenantHolder.setSuperAdmin(true);
            CurrentTenantHolder.setTenantId(tenantId);
            System.out.println("🚨 Super Admin detectado - Tenant: " + tenantId);
        } else if (tenantId != null) {
            CurrentTenantHolder.setSuperAdmin(false);
            CurrentTenantHolder.setTenantId(tenantId);
            System.out.println("👤 Usuario normal - Tenant: " + tenantId);
        } else {
            // Esquema por defecto para pruebas internas
            CurrentTenantHolder.setSuperAdmin(false);
            CurrentTenantHolder.setTenantId("public");
            System.out.println("⚠️ Sin tenant especificado, usando 'public'");
        }

        try {
            chain.doFilter(request, response);
        } finally {
            CurrentTenantHolder.clear();
        }
    }
}
