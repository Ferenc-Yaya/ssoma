package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TipoActivoDTO {
    private UUID tipoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
