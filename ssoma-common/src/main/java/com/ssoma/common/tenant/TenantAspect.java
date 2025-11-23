package com.ssoma.common.tenant;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TenantAspect {

    private final EntityManager entityManager;

    public static final String TENANT_FILTER_NAME = "tenantFilter";
    public static final String TENANT_PARAMETER = "tenantId";

    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public void activateTenantFilter() {
        if (TenantContext.hasTenant()) {
            Session session = entityManager.unwrap(Session.class);
            
            if (session.getEnabledFilter(TENANT_FILTER_NAME) == null) {
                session.enableFilter(TENANT_FILTER_NAME)
                        .setParameter(TENANT_PARAMETER, TenantContext.getTenantId());
                log.debug("Tenant filter activated for: {}", TenantContext.getTenantId());
            }
        }
    }

    public void disableTenantFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter(TENANT_FILTER_NAME);
        log.debug("Tenant filter disabled");
    }

    public void enableTenantFilter() {
        if (TenantContext.hasTenant()) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter(TENANT_FILTER_NAME)
                    .setParameter(TENANT_PARAMETER, TenantContext.getTenantId());
        }
    }
}
