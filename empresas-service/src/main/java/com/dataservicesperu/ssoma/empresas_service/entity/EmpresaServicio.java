package com.dataservicesperu.ssoma.empresas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "tbl_empresa_servicios")
@Data
@EqualsAndHashCode(exclude = "empresa")
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaServicio {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "servicio_id")
    private UUID servicioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "descripcion_servicio", nullable = false, columnDefinition = "TEXT")
    private String descripcionServicio;

    @Column(name = "nivel_riesgo", length = 50)
    private String nivelRiesgo;

    @Column(name = "activo")
    private Boolean activo = true;
}
