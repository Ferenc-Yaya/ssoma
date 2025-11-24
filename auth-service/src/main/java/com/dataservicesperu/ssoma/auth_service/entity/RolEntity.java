package com.dataservicesperu.ssoma.auth_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tbl_roles")
@Getter
@Setter
@NoArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rol_id")
    private UUID rolId;

    @Column(name = "codigo_rol", nullable = false, unique = true)
    private String codigoRol;

    @Column(name = "nombre_mostrar", nullable = false)
    private String nombreMostrar;
}
