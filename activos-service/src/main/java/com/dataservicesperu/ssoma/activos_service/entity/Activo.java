package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_activos")
@Data
public class Activo {

    @Id
    @Column(name = "activo_id")
    private UUID activoId;

    @Column(name = "categoria_id", nullable = false)
    private UUID categoriaId;

    @Column(name = "codigo", unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id", insertable = false, updatable = false)
    private CategoriaActivo categoria;

    @OneToMany(mappedBy = "activo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivoRequisito> requisitos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tbl_activos_operadores",
            joinColumns = @JoinColumn(name = "activo_id"),
            inverseJoinColumns = @JoinColumn(name = "operador_id")
    )
    private List<Operador> operadores = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (activoId == null) {
            activoId = UUID.randomUUID();
        }
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }
}
