package com.dataservicesperu.ssoma.common.tenant;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_EMPRESA_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> ES_HOST = new ThreadLocal<>();
    private static final ThreadLocal<String> CODIGO_ROL = new ThreadLocal<>();

    // Tenant ID
    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    // Empresa ID
    public static void setEmpresaId(String empresaId) {
        CURRENT_EMPRESA_ID.set(empresaId);
    }

    public static String getEmpresaId() {
        return CURRENT_EMPRESA_ID.get();
    }

    // Es Host
    public static void setEsHost(Boolean esHost) {
        ES_HOST.set(esHost);
    }

    public static Boolean getEsHost() {
        return ES_HOST.get();
    }

    public static boolean isHost() {
        return Boolean.TRUE.equals(ES_HOST.get());
    }

    // Código Rol
    public static void setCodigoRol(String rol) {
        CODIGO_ROL.set(rol);
    }

    public static String getCodigoRol() {
        return CODIGO_ROL.get();
    }

    public static boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(CODIGO_ROL.get());
    }

    // Clear all
    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_EMPRESA_ID.remove();
        ES_HOST.remove();
        CODIGO_ROL.remove();
    }
}
