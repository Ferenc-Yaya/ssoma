package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.TipoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TipoActivoRepository extends JpaRepository<TipoActivo, UUID> {
}
