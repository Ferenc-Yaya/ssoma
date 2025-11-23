package com.dataservicesperu.ssoma.activos_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_operadores")
@Data
public class Operador {

    @Id
    @Column(name = "operador_id")
    private UUID operadorId;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "documento", length = 50)
    private String documento;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToMany(mappedBy = "operadores")
    private List<Activo> activos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (operadorId == null) {
            operadorId = UUID.randomUUID();
        }
        if (activo == null) {
            activo = true;
        }
    }
}
