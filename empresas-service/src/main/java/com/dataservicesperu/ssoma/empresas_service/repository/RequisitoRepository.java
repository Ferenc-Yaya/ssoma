package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.Requisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequisitoRepository extends JpaRepository<Requisito, UUID> {

    Optional<Requisito> findByCodigo(String codigo);

    List<Requisito> findByActivoTrue();
}
