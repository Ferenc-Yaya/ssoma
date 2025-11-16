package com.dataservicesperu.ssoma.personas_service.repository;

import com.dataservicesperu.ssoma.personas_service.entity.SaludPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaludPersonaRepository extends JpaRepository<SaludPersona, UUID> {
    Optional<SaludPersona> findByPersonaId(UUID personaId);
    void deleteByPersonaId(UUID personaId);
}
