package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ActivoCreateUpdateDTO {
    private UUID categoriaId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    
    // IDs de operadores a asignar
    private List<UUID> operadorIds = new ArrayList<>();
    
    // Requisitos con su cumplimiento
    private List<RequisitoCreateDTO> requisitos = new ArrayList<>();
    
    @Data
    public static class RequisitoCreateDTO {
        private UUID requisitoId;
        private Boolean cumple;
        private String evidencia;
    }
}
