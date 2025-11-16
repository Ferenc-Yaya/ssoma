package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "tbl_empresa_categorias",
        uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_tipo_id", "categoria_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaCategoria {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "empresa_categoria_id")
    private UUID empresaCategoriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_tipo_id", nullable = false)
    private EmpresaTipo empresaTipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "aplica")
    private Boolean aplica = true;

    @Column(name = "activo")
    private Boolean activo = true;
}
