package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.RequisitoValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RequisitoValorRepository extends JpaRepository<RequisitoValor, UUID> {
    List<RequisitoValor> findByRequisitoId(UUID requisitoId);
}
