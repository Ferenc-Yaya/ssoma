package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_empresa_contactos")
@Data
@EqualsAndHashCode(exclude = "empresa")
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaContacto {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "contacto_id")
    private UUID contactoId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "tipo_contacto", nullable = false, length = 50)
    private String tipoContacto; // 'REPRESENTANTE_LEGAL', 'RESPONSABLE_EHS', 'ADMIN_CONTRATO'

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (esPrincipal == null) {
            esPrincipal = false;
        }
    }
}
