package com.dataservicesperu.ssoma.personas_service.repository;

import com.dataservicesperu.ssoma.personas_service.entity.Certificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificacionRepository extends JpaRepository<Certificacion, UUID> {
    List<Certificacion> findByPersonaId(UUID personaId);
    void deleteByPersonaId(UUID personaId);
}
