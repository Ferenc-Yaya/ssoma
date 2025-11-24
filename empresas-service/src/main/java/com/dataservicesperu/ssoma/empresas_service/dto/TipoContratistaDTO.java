package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoContratistaDTO {
    private UUID tipoId;
    private String codigo;
    private String nombre;
    private String descripcion;
}
