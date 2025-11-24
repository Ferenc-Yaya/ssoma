package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.TipoContratistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoContratistaRepository extends JpaRepository<TipoContratistaEntity, UUID> {

    Optional<TipoContratistaEntity> findByCodigo(String codigo);

    List<TipoContratistaEntity> findAll();
}
