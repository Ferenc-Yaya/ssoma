package com.dataservicesperu.ssoma.personas_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreatePersonaDTO {
    private UUID empresaId;
    private String tipoPersona;
    private String nombreCompleto;
    private String dni;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String correo;
}
