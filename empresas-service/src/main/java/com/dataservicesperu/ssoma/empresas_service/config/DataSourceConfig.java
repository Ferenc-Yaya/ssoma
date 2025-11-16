package com.dataservicesperu.ssoma.empresas_service.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String baseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        TenantDataSourceRouter router = new TenantDataSourceRouter();

        Map<Object, Object> targetDataSources = new HashMap<>();

        // Tenant A
        targetDataSources.put("tenant_a", createDataSource("tenant_a"));
        // Tenant B
        targetDataSources.put("tenant_b", createDataSource("tenant_b"));
        // Public (default)
        targetDataSources.put("public", createDataSource("public"));

        router.setTargetDataSources(targetDataSources);
        router.setDefaultTargetDataSource(createDataSource("public"));
        router.afterPropertiesSet();

        return router;
    }

    private DataSource createDataSource(String schema) {
        HikariDataSource dataSource = new HikariDataSource();
        String urlWithSchema = baseUrl.split("\\?")[0] + "?currentSchema=" + schema;
        
        dataSource.setJdbcUrl(urlWithSchema);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(2);
        dataSource.setPoolName("HikariPool-" + schema);

        System.out.println("========================================");
        System.out.println("=== DATASOURCE CONFIGURADO ===");
        System.out.println("Schema: " + schema);
        System.out.println("URL: " + urlWithSchema);
        System.out.println("User: " + username);
        System.out.println("========================================");

        return dataSource;
    }
}
