package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {
    private UUID contactoId;
    private String nombreCompleto;
    private String cargo;
    private String tipoContacto;
    private String email;
    private String telefono;
    private Boolean esPrincipal;
    private LocalDateTime createdAt;
}
