package com.dataservicesperu.ssoma.common.tenant;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TenantService {

    public String getTenantId() {
        return TenantContext.getTenantId();
    }

    public String getEmpresaId() {
        return TenantContext.getEmpresaId();
    }

    public UUID getEmpresaIdAsUUID() {
        String empresaId = TenantContext.getEmpresaId();
        return empresaId != null ? UUID.fromString(empresaId) : null;
    }

    public boolean isHost() {
        return TenantContext.isHost();
    }

    public String getCodigoRol() {
        return TenantContext.getCodigoRol();
    }

    public boolean isSuperAdmin() {
        return TenantContext.isSuperAdmin();
    }

    public boolean isAdminHost() {
        return "ADMIN_HOST".equals(TenantContext.getCodigoRol());
    }

    public boolean isAdminProveedor() {
        return "ADMIN_PROVEEDOR".equals(TenantContext.getCodigoRol());
    }
}
