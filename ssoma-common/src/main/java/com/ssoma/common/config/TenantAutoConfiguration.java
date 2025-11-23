package com.ssoma.common.config;

import com.ssoma.common.tenant.TenantAspect;
import com.ssoma.common.tenant.TenantInterceptor;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(EntityManager.class)
@EnableAspectJAutoProxy
@RequiredArgsConstructor
@Slf4j
public class TenantAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering TenantInterceptor for multi-tenancy support");
        registry.addInterceptor(tenantInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/public/**",
                        "/actuator/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantAspect tenantAspect(EntityManager entityManager) {
        log.info("Configuring TenantAspect for automatic tenant filtering");
        return new TenantAspect(entityManager);
    }
}
