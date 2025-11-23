package com.ssoma.common.tenant;

public class TenantNotSetException extends RuntimeException {

    public TenantNotSetException(String message) {
        super(message);
    }

    public TenantNotSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
