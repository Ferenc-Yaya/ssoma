package com.dataservicesperu.ssoma.auth_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String tenantId;
    private String role;
}
