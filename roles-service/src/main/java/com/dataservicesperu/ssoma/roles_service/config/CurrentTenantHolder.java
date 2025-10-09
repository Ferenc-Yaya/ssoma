package com.dataservicesperu.ssoma.roles_service.config;

public class CurrentTenantHolder {

    private static final ThreadLocal<String> tenantHolder = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        tenantHolder.set(tenantId);
    }

    public static String getTenantId() {
        return tenantHolder.get();
    }

    public static void clear() {
        tenantHolder.remove();
    }
}
