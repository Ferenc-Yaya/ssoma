package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaContactoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaContactoRepository extends JpaRepository<EmpresaContactoEntity, UUID> {

    List<EmpresaContactoEntity> findByEmpresa_EmpresaId(UUID empresaId);

    Optional<EmpresaContactoEntity> findByEmpresa_EmpresaIdAndEsPrincipalTrue(UUID empresaId);

    List<EmpresaContactoEntity> findByEmpresa_EmpresaIdAndTipoContacto(UUID empresaId, String tipoContacto);
}