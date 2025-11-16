package com.dataservicesperu.ssoma.personas_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_certificaciones")
@Data
public class Certificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "certificacion_id")
    private UUID certificacionId;

    @Column(name = "persona_id", nullable = false)
    private UUID personaId;

    @Column(name = "nombre_certificacion", nullable = false, length = 100)
    private String nombreCertificacion;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
}
