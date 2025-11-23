package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.Requisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RequisitoRepository extends JpaRepository<Requisito, UUID> {
    List<Requisito> findByCategoriaId(UUID categoriaId);
}
