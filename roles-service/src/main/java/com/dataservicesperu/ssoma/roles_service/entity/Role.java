package com.dataservicesperu.ssoma.roles_service.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rol_id")
    private UUID rolId;

    @Column(name = "nombre_rol", nullable = false, length = 100)
    private String nombreRol;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    public Role() {}

    public Role(String nombreRol, String descripcion) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public UUID getRolId() {
        return rolId;
    }

    public void setRolId(UUID rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
