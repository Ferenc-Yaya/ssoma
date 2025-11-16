package com.dataservicesperu.ssoma.personas_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SaludPersonaDTO {
    private UUID saludId;
    private UUID personaId;
    private String grupoSanguineo;
    private String alergias;
    private String historialMedico;
}
