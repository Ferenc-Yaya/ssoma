package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    Optional<Categoria> findByCodigo(String codigo);

    List<Categoria> findByActivoTrue();
}
