package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    Optional<Empresa> findByTenantIdAndRuc(String tenantId, String ruc);

    boolean existsByTenantIdAndRuc(String tenantId, String ruc);

    List<Empresa> findByTenantIdAndActivoTrue(String tenantId);

    List<Empresa> findByTenantIdAndRubroComercial(String tenantId, String rubroComercial);

    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.contactos LEFT JOIN FETCH e.tipo " +
           "WHERE e.empresaId = :id AND e.tenantId = :tenantId")
    Optional<Empresa> findByIdAndTenantIdWithDetails(@Param("id") UUID id, @Param("tenantId") String tenantId);

    Optional<Empresa> findByEmpresaIdAndTenantId(UUID empresaId, String tenantId);

    List<Empresa> findByTenantIdAndEsHostTrue(String tenantId);
}
