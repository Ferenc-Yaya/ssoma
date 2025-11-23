package com.dataservicesperu.ssoma.auth_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "persona_id")
    private UUID personaId;

    @Column(name = "empresa_id")
    private UUID empresaId;

    @Column(name = "es_host")
    private Boolean esHost = false;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "rol_id")
    private UUID rolId;

    @Column(name = "codigo_rol")
    private String codigoRol;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}