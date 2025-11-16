package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaTipoRepository extends JpaRepository<EmpresaTipo, UUID> {

    List<EmpresaTipo> findByEmpresa_EmpresaIdAndActivoTrue(UUID empresaId);

    Optional<EmpresaTipo> findByEmpresa_EmpresaIdAndTipoEmpresa_TipoId(UUID empresaId, UUID tipoId);

    @Query("SELECT et FROM EmpresaTipo et LEFT JOIN FETCH et.requisitosPersonalizados WHERE et.empresaTipoId = :id")
    Optional<EmpresaTipo> findByIdWithRequisitos(@Param("id") UUID id);
}
