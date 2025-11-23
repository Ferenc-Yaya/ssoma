package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.ActivoRequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivoRequisitoRepository extends JpaRepository<ActivoRequisito, UUID> {
    List<ActivoRequisito> findByActivoId(UUID activoId);
}
