package com.ssoma.common.tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantService {

    private final TenantAspect tenantAspect;

    public <T> T executeInTenant(String tenantId, Supplier<T> operation) {
        String previousTenant = TenantContext.getTenantId();
        try {
            TenantContext.setTenantId(tenantId);
            tenantAspect.enableTenantFilter();
            return operation.get();
        } finally {
            if (previousTenant != null) {
                TenantContext.setTenantId(previousTenant);
                tenantAspect.enableTenantFilter();
            } else {
                TenantContext.clear();
            }
        }
    }

    public <T> T executeWithoutTenantFilter(Supplier<T> operation) {
        try {
            tenantAspect.disableTenantFilter();
            log.warn("Executing operation WITHOUT tenant filter - admin mode");
            return operation.get();
        } finally {
            tenantAspect.enableTenantFilter();
        }
    }

    public void runInTenant(String tenantId, Runnable action) {
        executeInTenant(tenantId, () -> {
            action.run();
            return null;
        });
    }

    public void runWithoutTenantFilter(Runnable action) {
        executeWithoutTenantFilter(() -> {
            action.run();
            return null;
        });
    }

    public String getCurrentTenant() {
        return TenantContext.getTenantId();
    }

    public boolean hasTenant() {
        return TenantContext.hasTenant();
    }
}
