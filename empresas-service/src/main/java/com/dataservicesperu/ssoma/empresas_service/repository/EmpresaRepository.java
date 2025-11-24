package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, UUID> {

    Optional<EmpresaEntity> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

    List<EmpresaEntity> findByActivoTrue();

    List<EmpresaEntity> findByRubroComercial(String rubroComercial);

    @Query("SELECT e FROM EmpresaEntity e LEFT JOIN FETCH e.contactos LEFT JOIN FETCH e.tipo WHERE e.empresaId = :id")
    Optional<EmpresaEntity> findByIdWithDetails(@Param("id") UUID id);

    List<EmpresaEntity> findByEsHostTrue();
}