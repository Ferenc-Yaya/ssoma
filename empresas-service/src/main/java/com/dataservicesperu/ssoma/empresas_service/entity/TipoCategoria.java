package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "cat_tipo_categorias",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_id", "categoria_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCategoria {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "tipo_categoria_id")
    private UUID tipoCategoriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoContratista tipoContratista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "activo")
    private Boolean activo = true;
}
