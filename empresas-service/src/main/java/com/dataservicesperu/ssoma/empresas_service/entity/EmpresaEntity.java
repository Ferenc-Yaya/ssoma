package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tbl_empresas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "ruc"}))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@EqualsAndHashCode(exclude = {"contactos", "tipo"})
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "empresa_id", updatable = false, nullable = false)
    private UUID empresaId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "ruc", nullable = false, length = 20)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_id")
    private TipoContratistaEntity tipo;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "sitio_web", length = 100)
    private String sitioWeb;

    @Column(name = "rubro_comercial", length = 100)
    private String rubroComercial;

    @Column(name = "score_seguridad")
    private Integer scoreSeguridad = 100;

    @Column(name = "estado_habilitacion", length = 20)
    private String estadoHabilitacion = "PENDIENTE";

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "es_host")
    private Boolean esHost = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EmpresaContactoEntity> contactos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
        if (scoreSeguridad == null) {
            scoreSeguridad = 100;
        }
        if (estadoHabilitacion == null) {
            estadoHabilitacion = "PENDIENTE";
        }
        if (esHost == null) {
            esHost = false;
        }
    }
}