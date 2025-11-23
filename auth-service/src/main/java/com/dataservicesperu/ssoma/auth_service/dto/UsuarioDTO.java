package com.dataservicesperu.ssoma.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioDTO {
    private UUID usuarioId;
    private String tenantId;
    private UUID personaId;
    private UUID empresaId;
    private Boolean esHost;
    private String username;
    private String password;
    private String codigoRol;
    private Boolean activo;
}
