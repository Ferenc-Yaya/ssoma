package com.dataservicesperu.ssoma.personas_service.repository;

import com.dataservicesperu.ssoma.personas_service.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {
    List<Persona> findByEmpresaId(UUID empresaId);
    List<Persona> findByTipoPersona(String tipoPersona);
}
