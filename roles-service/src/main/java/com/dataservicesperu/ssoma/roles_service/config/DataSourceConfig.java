package com.dataservicesperu.ssoma.roles_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        TenantDataSourceRouter router = new TenantDataSourceRouter();

        Map<Object, Object> targetDataSources = new HashMap<>();

        // DataSource para tenant_a
        targetDataSources.put("tenant_a", createDataSource("tenant_a"));

        // DataSource para tenant_b
        targetDataSources.put("tenant_b", createDataSource("tenant_b"));

        // DataSource por defecto (usa schema public)
        DataSource defaultDataSource = createDataSource("public");

        router.setTargetDataSources(targetDataSources);
        router.setDefaultTargetDataSource(defaultDataSource);
        router.afterPropertiesSet();

        return router;
    }

    private DataSource createDataSource(String schema) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/ssoma_db?currentSchema=" + schema);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
}
