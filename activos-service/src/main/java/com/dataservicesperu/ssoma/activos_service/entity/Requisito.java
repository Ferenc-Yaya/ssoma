package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "cat_requisitos")
@Data
public class Requisito {

    @Id
    @Column(name = "requisito_id")
    private UUID requisitoId;

    @Column(name = "categoria_id", nullable = false)
    private UUID categoriaId;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "obligatorio")
    private Boolean obligatorio = true;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id", insertable = false, updatable = false)
    private CategoriaActivo categoria;

    @PrePersist
    public void prePersist() {
        if (requisitoId == null) {
            requisitoId = UUID.randomUUID();
        }
        if (obligatorio == null) {
            obligatorio = true;
        }
        if (activo == null) {
            activo = true;
        }
    }
}
