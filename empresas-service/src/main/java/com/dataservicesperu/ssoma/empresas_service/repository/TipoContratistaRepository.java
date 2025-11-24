package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.TipoContratista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoContratistaRepository extends JpaRepository<TipoContratista, UUID> {

    Optional<TipoContratista> findByCodigo(String codigo);

    List<TipoContratista> findAll();
}
