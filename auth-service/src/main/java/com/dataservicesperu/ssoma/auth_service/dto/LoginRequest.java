package com.dataservicesperu.ssoma.auth_service.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String nombreUsuario;
    private String password;
}
