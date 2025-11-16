package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaRequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRequisitoRepository extends JpaRepository<EmpresaRequisito, UUID> {

    List<EmpresaRequisito> findByEmpresaTipo_EmpresaTipoIdAndActivoTrue(UUID empresaTipoId);

    Optional<EmpresaRequisito> findByEmpresaTipo_EmpresaTipoIdAndRequisito_CategoriaId(UUID empresaTipoId, UUID categoriaId);
}
