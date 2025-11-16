package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {
    private UUID contactoId;
    private String nombreContacto;
    private String telefono;
    private String email;
    private Boolean esPrincipal;
    private Boolean activo;
}
