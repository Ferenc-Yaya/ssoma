package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ActivoDTO {
    private UUID activoId;
    private UUID categoriaId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
    
    // Información adicional de relaciones
    private String categoriaNombre;
    private String tipoNombre;
    
    // Listas de relaciones
    private List<ActivoRequisitoDTO> requisitos = new ArrayList<>();
    private List<OperadorSimpleDTO> operadores = new ArrayList<>();
}
