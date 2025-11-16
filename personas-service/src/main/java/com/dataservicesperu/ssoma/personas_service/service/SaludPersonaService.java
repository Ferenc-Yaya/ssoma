package com.dataservicesperu.ssoma.personas_service.service;

import com.dataservicesperu.ssoma.personas_service.dto.SaludPersonaDTO;
import com.dataservicesperu.ssoma.personas_service.entity.SaludPersona;
import com.dataservicesperu.ssoma.personas_service.mapper.SaludPersonaMapper;
import com.dataservicesperu.ssoma.personas_service.repository.PersonaRepository;
import com.dataservicesperu.ssoma.personas_service.repository.SaludPersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaludPersonaService {

    private final SaludPersonaRepository saludPersonaRepository;
    private final PersonaRepository personaRepository;
    private final SaludPersonaMapper saludPersonaMapper;

    /**
     * Listar todos los registros de salud
     */
    @Transactional(readOnly = true)
    public List<SaludPersonaDTO> listarTodas() {
        List<SaludPersona> registros = saludPersonaRepository.findAll();
        return saludPersonaMapper.toDTOList(registros);
    }

    /**
     * Obtener registro de salud por ID
     */
    @Transactional(readOnly = true)
    public SaludPersonaDTO obtenerPorId(UUID id) {
        SaludPersona salud = saludPersonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de salud no encontrado con ID: " + id));
        return saludPersonaMapper.toDTO(salud);
    }

    /**
     * Obtener registro de salud por persona
     */
    @Transactional(readOnly = true)
    public SaludPersonaDTO obtenerPorPersonaId(UUID personaId) {
        SaludPersona salud = saludPersonaRepository.findByPersonaId(personaId)
                .orElseThrow(() -> new RuntimeException("Registro de salud no encontrado para la persona: " + personaId));
        return saludPersonaMapper.toDTO(salud);
    }

    /**
     * Crear nuevo registro de salud
     */
    @Transactional
    public SaludPersonaDTO crear(SaludPersonaDTO saludPersonaDTO) {
        // Validar que la persona existe
        if (!personaRepository.existsById(saludPersonaDTO.getPersonaId())) {
            throw new RuntimeException("La persona no existe con ID: " + saludPersonaDTO.getPersonaId());
        }

        // Validar que no exista ya un registro de salud para esta persona
        if (saludPersonaRepository.findByPersonaId(saludPersonaDTO.getPersonaId()).isPresent()) {
            throw new RuntimeException("Ya existe un registro de salud para esta persona");
        }

        SaludPersona salud = saludPersonaMapper.toEntity(saludPersonaDTO);
        salud.setSaludId(UUID.randomUUID());

        SaludPersona guardada = saludPersonaRepository.save(salud);
        return saludPersonaMapper.toDTO(guardada);
    }

    /**
     * Actualizar registro de salud
     */
    @Transactional
    public SaludPersonaDTO actualizar(UUID id, SaludPersonaDTO saludPersonaDTO) {
        SaludPersona salud = saludPersonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de salud no encontrado con ID: " + id));

        saludPersonaMapper.updateEntityFromDTO(saludPersonaDTO, salud);

        SaludPersona actualizada = saludPersonaRepository.save(salud);
        return saludPersonaMapper.toDTO(actualizada);
    }

    /**
     * Eliminar registro de salud
     */
    @Transactional
    public void eliminar(UUID id) {
        if (!saludPersonaRepository.existsById(id)) {
            throw new RuntimeException("Registro de salud no encontrado con ID: " + id);
        }
        saludPersonaRepository.deleteById(id);
    }

    /**
     * Eliminar registro de salud por persona
     */
    @Transactional
    public void eliminarPorPersonaId(UUID personaId) {
        saludPersonaRepository.deleteByPersonaId(personaId);
    }
}
