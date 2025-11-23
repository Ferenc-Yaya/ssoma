package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OperadorSimpleDTO {
    private UUID operadorId;
    private String nombre;
    private String documento;
    private Boolean activo;
}
