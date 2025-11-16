package com.dataservicesperu.ssoma.personas_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CertificacionDTO {
    private UUID certificacionId;
    private UUID personaId;
    private String nombreCertificacion;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
}
