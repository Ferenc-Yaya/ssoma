package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tbl_empresa_tipo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_id", "tipo_id"}))
@Data
@EqualsAndHashCode(exclude = {"empresa", "tipoContratista", "categoriasPersonalizadas"})
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaTipo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "empresa_tipo_id")
    private UUID empresaTipoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoContratista tipoContratista;

    @Column(name = "activo")
    private Boolean activo = true;

    @OneToMany(mappedBy = "empresaTipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EmpresaCategoria> categoriasPersonalizadas = new HashSet<>();
}
