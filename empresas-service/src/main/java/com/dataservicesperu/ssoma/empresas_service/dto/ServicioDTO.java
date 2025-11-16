package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private UUID servicioId;
    private String descripcionServicio;
    private String nivelRiesgo;
    private Boolean activo;
}
