package com.dataservicesperu.ssoma.empresas_service.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantDataSourceRouter extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = CurrentTenantHolder.getTenantId();
        System.out.println("🔍 Determinando DataSource para tenant: " + tenantId);
        return tenantId != null ? tenantId : "public";
    }
}
