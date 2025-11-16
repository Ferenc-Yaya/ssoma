package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cat_tipo_contratista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoContratista {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "tipo_id")
    private UUID tipoId;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo = true;

    @OneToMany(mappedBy = "tipoContratista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TipoCategoria> categorias = new HashSet<>();
}
