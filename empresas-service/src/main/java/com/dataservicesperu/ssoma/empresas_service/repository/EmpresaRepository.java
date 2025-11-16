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

    Optional<Empresa> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

    List<Empresa> findByActivoTrue();

    List<Empresa> findBySector(String sector);

    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.contactos LEFT JOIN FETCH e.servicios WHERE e.empresaId = :id")
    Optional<Empresa> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.tipos WHERE e.empresaId = :id")
    Optional<Empresa> findByIdWithTipos(@Param("id") UUID id);
}
