package com.dataservicesperu.ssoma.personas_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_score_seguridad")
@Data
public class ScoreSeguridad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "score_id")
    private UUID scoreId;

    @Column(name = "persona_id", nullable = false)
    private UUID personaId;

    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(name = "motivo_cambio", columnDefinition = "TEXT")
    private String motivoCambio;
}
