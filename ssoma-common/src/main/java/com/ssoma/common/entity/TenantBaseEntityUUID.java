package com.ssoma.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Clase base para entidades con tenant y UUID como ID.
 * Extiende TenantBaseEntity agregando el campo id UUID.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class TenantBaseEntityUUID extends TenantBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
