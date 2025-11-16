package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaContactoRepository extends JpaRepository<EmpresaContacto, UUID> {

    List<EmpresaContacto> findByEmpresa_EmpresaIdAndActivoTrue(UUID empresaId);

    Optional<EmpresaContacto> findByEmpresa_EmpresaIdAndEsPrincipalTrue(UUID empresaId);
}
