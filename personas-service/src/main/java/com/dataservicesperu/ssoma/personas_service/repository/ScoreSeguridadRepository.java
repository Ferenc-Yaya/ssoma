package com.dataservicesperu.ssoma.personas_service.repository;

import com.dataservicesperu.ssoma.personas_service.entity.ScoreSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreSeguridadRepository extends JpaRepository<ScoreSeguridad, UUID> {
    List<ScoreSeguridad> findByPersonaId(UUID personaId);
    void deleteByPersonaId(UUID personaId);
}
