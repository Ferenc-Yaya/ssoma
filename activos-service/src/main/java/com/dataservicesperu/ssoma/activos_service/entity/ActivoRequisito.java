package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_activos_requisitos")
@Data
public class ActivoRequisito {

    @Id
    @Column(name = "activo_requisito_id")
    private UUID activoRequisitoId;

    @Column(name = "activo_id", nullable = false)
    private UUID activoId;

    @Column(name = "requisito_id", nullable = false)
    private UUID requisitoId;

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "evidencia", columnDefinition = "TEXT")
    private String evidencia;

    @Column(name = "fecha_verificacion")
    private LocalDate fechaVerificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activo_id", referencedColumnName = "activo_id", insertable = false, updatable = false)
    private Activo activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisito_id", referencedColumnName = "requisito_id", insertable = false, updatable = false)
    private Requisito requisito;

    @PrePersist
    public void prePersist() {
        if (activoRequisitoId == null) {
            activoRequisitoId = UUID.randomUUID();
        }
        if (fechaVerificacion == null) {
            fechaVerificacion = LocalDate.now();
        }
    }
}
