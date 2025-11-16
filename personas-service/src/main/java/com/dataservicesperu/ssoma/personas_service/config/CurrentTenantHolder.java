package com.dataservicesperu.ssoma.personas_service.config;

public class CurrentTenantHolder {

    private static final ThreadLocal<String> tenantHolder = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> superAdminHolder = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        tenantHolder.set(tenantId);
    }

    public static String getTenantId() {
        return tenantHolder.get();
    }

    public static void setSuperAdmin(Boolean isSuperAdmin) {
        superAdminHolder.set(isSuperAdmin);
    }

    public static Boolean isSuperAdmin() {
        return Boolean.TRUE.equals(superAdminHolder.get());
    }

    public static void clear() {
        tenantHolder.remove();
        superAdminHolder.remove();
    }
}
