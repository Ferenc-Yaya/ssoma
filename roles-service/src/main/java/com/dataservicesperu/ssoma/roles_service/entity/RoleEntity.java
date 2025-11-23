package com.dataservicesperu.ssoma.roles_service.entity;

import com.ssoma.common.entity.TenantBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.UUID;

@Entity
@Table(name = "tbl_roles")
@Getter
@Setter
@NoArgsConstructor
@Filter(name = "tenantFilter")
public class RoleEntity extends TenantBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rol_id")
    private UUID rolId;

    @Column(name = "codigo_rol", nullable = false, unique = true, length = 50)
    private String codigoRol;

    @Column(name = "nombre_mostrar", nullable = false, length = 100)
    private String nombreMostrar;
}
