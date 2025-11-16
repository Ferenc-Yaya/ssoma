package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "nombre_contacto", length = 200)
    private String nombreContacto;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "activo")
    private Boolean activo = true;
}
