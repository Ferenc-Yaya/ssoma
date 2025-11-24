package com.dataservicesperu.ssoma.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RolDTO {
    private UUID rolId;
    private String codigoRol;
    private String nombreMostrar;
}
