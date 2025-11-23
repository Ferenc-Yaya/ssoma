package com.dataservicesperu.ssoma.common.tenant;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<UUID> EMPRESA_CONTRATISTA = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IS_EMPRESA_HOST = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        log.debug("Setting tenant to: {}", tenantId);
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void setEmpresaContratista(UUID empresaId) {
        log.debug("Setting empresa contratista to: {}", empresaId);
        EMPRESA_CONTRATISTA.set(empresaId);
    }

    public static UUID getEmpresaContratista() {
        return EMPRESA_CONTRATISTA.get();
    }

    public static void setEmpresaHost(Boolean isHost) {
        log.debug("Setting is empresa host to: {}", isHost);
        IS_EMPRESA_HOST.set(isHost);
    }

    public static boolean isEmpresaHost() {
        return Boolean.TRUE.equals(IS_EMPRESA_HOST.get());
    }

    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }

    public static boolean hasEmpresa() {
        return EMPRESA_CONTRATISTA.get() != null;
    }

    public static void clear() {
        log.debug("Clearing tenant context");
        CURRENT_TENANT.remove();
        EMPRESA_CONTRATISTA.remove();
        IS_EMPRESA_HOST.remove();
    }

    public static String requireTenantId() {
        String tenantId = CURRENT_TENANT.get();
        if (tenantId == null || tenantId.isBlank()) {
            throw new TenantNotSetException("No tenant set in current context");
        }
        return tenantId;
    }

    public static UUID requireEmpresaContratista() {
        UUID empresaId = EMPRESA_CONTRATISTA.get();
        if (empresaId == null) {
            throw new TenantNotSetException("No empresa contratista set in current context");
        }
        return empresaId;
    }
}
