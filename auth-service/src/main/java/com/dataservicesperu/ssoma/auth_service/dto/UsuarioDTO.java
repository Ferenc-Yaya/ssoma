package com.dataservicesperu.ssoma.auth_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UsuarioDTO {
    private UUID usuarioId;
    private String tenantId;
    private UUID personaId;
    private UUID empresaId;
    private Boolean esHost;
    private String username;
    private String password;
    private UUID rolId;
    private String codigoRol;
    private String nombreRol;
    private Boolean activo;
    private LocalDateTime createdAt;
}
