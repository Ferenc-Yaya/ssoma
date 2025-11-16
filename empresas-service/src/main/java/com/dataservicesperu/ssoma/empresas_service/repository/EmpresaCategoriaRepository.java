package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaCategoriaRepository extends JpaRepository<EmpresaCategoria, UUID> {

    List<EmpresaCategoria> findByEmpresaTipo_EmpresaTipoIdAndActivoTrue(UUID empresaTipoId);

    Optional<EmpresaCategoria> findByEmpresaTipo_EmpresaTipoIdAndCategoria_CategoriaId(UUID empresaTipoId, UUID categoriaId);
}
