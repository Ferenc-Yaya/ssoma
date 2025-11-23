package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoriaActivoDTO {
    private UUID categoriaId;
    private UUID tipoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String tipoNombre; // Para mostrar el nombre del tipo
}
