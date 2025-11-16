package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TipoCategoriaRepository extends JpaRepository<TipoCategoria, UUID> {

    List<TipoCategoria> findByTipoContratista_TipoIdAndActivoTrue(UUID tipoId);
}
