package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "cat_categorias_activo")
@Data
public class CategoriaActivo {

    @Id
    @Column(name = "categoria_id")
    private UUID categoriaId;

    @Column(name = "tipo_id", nullable = false)
    private UUID tipoId;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", referencedColumnName = "tipo_id", insertable = false, updatable = false)
    private TipoActivo tipo;

    @PrePersist
    public void prePersist() {
        if (categoriaId == null) {
            categoriaId = UUID.randomUUID();
        }
        if (activo == null) {
            activo = true;
        }
    }
}
