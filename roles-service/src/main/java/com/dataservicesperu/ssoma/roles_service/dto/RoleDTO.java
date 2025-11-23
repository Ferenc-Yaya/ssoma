package com.dataservicesperu.ssoma.roles_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleDTO {
    private UUID rolId;
    private String codigoRol;
    private String nombreMostrar;
    private String tenantId;
}