package com.dataservicesperu.ssoma.activos_service.repository;

import com.dataservicesperu.ssoma.activos_service.entity.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, UUID> {
}
