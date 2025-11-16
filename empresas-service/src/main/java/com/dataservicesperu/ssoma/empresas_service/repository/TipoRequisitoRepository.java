package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.TipoRequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TipoRequisitoRepository extends JpaRepository<TipoRequisito, UUID> {

    List<TipoRequisito> findByTipoEmpresa_TipoIdAndActivoTrue(UUID tipoId);
}
