package com.dataservicesperu.ssoma.common.tenant;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantAspect {

    private final EntityManager entityManager;

    @Before("execution(* com.dataservicesperu.ssoma..repository.*Repository.*(..))")
    public void enableTenantFilter() {
        // SUPER_ADMIN no tiene filtro de tenant - ve todo el sistema
        if (TenantContext.isSuperAdmin()) {
            log.debug("SUPER_ADMIN detected - skipping tenant filter");
            disableTenantFilter();
            return;
        }

        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("No tenant ID in context - filter not applied");
            return;
        }

        try {
            Session session = entityManager.unwrap(Session.class);
            org.hibernate.Filter filter = session.enableFilter("tenantFilter");
            filter.setParameter("tenantId", tenantId);
            log.debug("Tenant filter enabled for tenant: {}", tenantId);
        } catch (Exception e) {
            log.error("Error enabling tenant filter: {}", e.getMessage());
        }
    }

    private void disableTenantFilter() {
        try {
            Session session = entityManager.unwrap(Session.class);
            session.disableFilter("tenantFilter");
            log.debug("Tenant filter disabled");
        } catch (Exception e) {
            log.debug("Could not disable tenant filter: {}", e.getMessage());
        }
    }
}
