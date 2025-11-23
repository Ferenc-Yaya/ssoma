package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "cat_tipos_activo")
@Data
public class TipoActivo {

    @Id
    @Column(name = "tipo_id")
    private UUID tipoId;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        if (tipoId == null) {
            tipoId = UUID.randomUUID();
        }
        if (activo == null) {
            activo = true;
        }
    }
}
