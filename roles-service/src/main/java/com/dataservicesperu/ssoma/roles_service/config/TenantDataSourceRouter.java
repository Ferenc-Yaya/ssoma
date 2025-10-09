package com.dataservicesperu.ssoma.roles_service.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantDataSourceRouter extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return CurrentTenantHolder.getTenantId();
    }
}
