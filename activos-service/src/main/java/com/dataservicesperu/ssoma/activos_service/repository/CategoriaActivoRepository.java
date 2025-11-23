package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.CategoriaActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CategoriaActivoRepository extends JpaRepository<CategoriaActivo, UUID> {
    List<CategoriaActivo> findByTipoId(UUID tipoId);
}
