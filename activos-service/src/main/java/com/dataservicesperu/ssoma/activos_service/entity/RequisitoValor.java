package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cat_requisitos_valores")
@Data
public class RequisitoValor {

    @Id
    @Column(name = "valor_id")
    private UUID valorId;

    @Column(name = "requisito_id", nullable = false)
    private UUID requisitoId;

    @Column(name = "valor_texto", length = 300)
    private String valorTexto;

    @Column(name = "valor_numerico")
    private BigDecimal valorNumerico;

    @Column(name = "valor_fecha")
    private LocalDate valorFecha;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisito_id", referencedColumnName = "requisito_id", insertable = false, updatable = false)
    private Requisito requisito;

    @PrePersist
    public void prePersist() {
        if (valorId == null) {
            valorId = UUID.randomUUID();
        }
        if (activo == null) {
            activo = true;
        }
    }
}
