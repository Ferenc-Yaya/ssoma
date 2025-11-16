package com.dataservicesperu.ssoma.empresas_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = httpRequest.getHeader("X-Tenant-ID");

        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = "tenant_a"; // Default tenant para desarrollo
        }

        System.out.println("🎯 TenantFilter: Estableciendo tenant = " + tenantId);
        CurrentTenantHolder.setTenantId(tenantId);

        try {
            chain.doFilter(request, response);
        } finally {
            CurrentTenantHolder.clear();
        }
    }
}
