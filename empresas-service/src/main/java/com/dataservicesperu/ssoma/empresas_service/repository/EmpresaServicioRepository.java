package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmpresaServicioRepository extends JpaRepository<EmpresaServicio, UUID> {

    List<EmpresaServicio> findByEmpresa_EmpresaIdAndActivoTrue(UUID empresaId);
}
