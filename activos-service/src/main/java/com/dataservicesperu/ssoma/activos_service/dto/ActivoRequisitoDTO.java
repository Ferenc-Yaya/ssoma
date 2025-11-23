package com.dataservicesperu.ssoma.activos_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ActivoRequisitoDTO {
    private UUID activoRequisitoId;
    private UUID activoId;
    private UUID requisitoId;
    private Boolean cumple;
    private String evidencia;
    private LocalDate fechaVerificacion;
    
    // Información adicional del requisito
    private String requisitoNombre;
    private String requisitoCodigo;
    private Boolean requisitoObligatorio;
}
