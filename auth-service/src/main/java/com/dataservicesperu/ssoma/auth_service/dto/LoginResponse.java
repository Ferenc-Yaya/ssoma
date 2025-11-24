package com.dataservicesperu.ssoma.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String tenantId;
    private UUID empresaId;
    private Boolean esHost;
    private String codigoRol;
}
