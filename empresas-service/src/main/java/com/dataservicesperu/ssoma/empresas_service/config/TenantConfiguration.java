package com.dataservicesperu.ssoma.empresas_service.config;

import com.dataservicesperu.ssoma.common.tenant.TenantAspect;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class TenantConfiguration {

    @Bean
    public TenantAspect tenantAspect(EntityManager entityManager) {
        return new TenantAspect(entityManager);
    }
}