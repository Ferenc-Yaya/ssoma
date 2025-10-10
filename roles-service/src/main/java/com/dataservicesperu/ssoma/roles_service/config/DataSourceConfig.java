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

        // ✅ SIN DEFAULTS - lee directamente las variables
        // Si no existen, lanzará excepción (lo cual es correcto)
        String host = System.getenv("POSTGRES_HOST");
        String port = System.getenv("POSTGRES_PORT");
        String database = System.getenv("POSTGRES_DB");
        String username = System.getenv("POSTGRES_USER");
        String password = System.getenv("POSTGRES_PASSWORD");

        // Validación (opcional pero recomendado)
        if (host == null || port == null || database == null) {
            throw new IllegalStateException(
                    "Las variables de entorno POSTGRES_HOST, POSTGRES_PORT y POSTGRES_DB son obligatorias. " +
                            "Deben definirse en docker-compose.yml"
            );
        }

        String url = String.format("jdbc:postgresql://%s:%s/%s?currentSchema=%s",
                host, port, database, schema);

        System.out.println("========================================");
        System.out.println("=== DATASOURCE CONFIGURADO ===");
        System.out.println("Schema: " + schema);
        System.out.println("URL: " + url);
        System.out.println("User: " + username);
        System.out.println("========================================");

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
