package com.dataservicesperu.ssoma.personas_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PersonaDetalleDTO {
    // Datos básicos de persona
    private UUID personaId;
    private UUID empresaId;
    private String tipoPersona;
    private String nombreCompleto;
    private String dni;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String correo;

    // Información de salud
    private SaludPersonaDTO salud;

    // Certificaciones
    private List<CertificacionDTO> certificaciones;
}
