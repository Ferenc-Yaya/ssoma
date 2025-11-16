package com.dataservicesperu.ssoma.personas_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tbl_salud_personas")
@Data
public class SaludPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "salud_id")
    private UUID saludId;

    @Column(name = "persona_id", nullable = false)
    private UUID personaId;

    @Column(name = "grupo_sanguineo", length = 10)
    private String grupoSanguineo;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "historial_medico", columnDefinition = "TEXT")
    private String historialMedico;
}
