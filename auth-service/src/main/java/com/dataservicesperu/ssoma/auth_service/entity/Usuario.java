package com.dataservicesperu.ssoma.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tbl_usuarios", schema = "public")
@Data
public class Usuario {

    @Id
    @Column(name = "usuario_id")
    private UUID id;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "rol", nullable = false)
    private String role;
}