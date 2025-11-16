package com.dataservicesperu.ssoma.personas_service.service;

import com.dataservicesperu.ssoma.personas_service.dto.CreatePersonaDTO;
import com.dataservicesperu.ssoma.personas_service.dto.PersonaDTO;
import com.dataservicesperu.ssoma.personas_service.dto.PersonaDetalleDTO;
import com.dataservicesperu.ssoma.personas_service.entity.Persona;
import com.dataservicesperu.ssoma.personas_service.mapper.CertificacionMapper;
import com.dataservicesperu.ssoma.personas_service.mapper.PersonaMapper;
import com.dataservicesperu.ssoma.personas_service.mapper.SaludPersonaMapper;
import com.dataservicesperu.ssoma.personas_service.repository.CertificacionRepository;
import com.dataservicesperu.ssoma.personas_service.repository.PersonaRepository;
import com.dataservicesperu.ssoma.personas_service.repository.SaludPersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final SaludPersonaRepository saludPersonaRepository;
    private final CertificacionRepository certificacionRepository;
    private final PersonaMapper personaMapper;
    private final SaludPersonaMapper saludPersonaMapper;
    private final CertificacionMapper certificacionMapper;

    /**
     * Listar todas las personas del tenant actual
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> listarTodas() {
        List<Persona> personas = personaRepository.findAll();
        return personaMapper.toDTOList(personas);
    }

    /**
     * Obtener persona por ID
     */
    @Transactional(readOnly = true)
    public PersonaDTO obtenerPorId(UUID id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
        return personaMapper.toDTO(persona);
    }

    /**
     * Obtener persona con todos sus detalles (salud y certificaciones)
     */
    @Transactional(readOnly = true)
    public PersonaDetalleDTO obtenerDetallePorId(UUID id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));

        PersonaDetalleDTO detalle = new PersonaDetalleDTO();

        // Datos básicos
        PersonaDTO personaDTO = personaMapper.toDTO(persona);
        detalle.setPersonaId(personaDTO.getPersonaId());
        detalle.setEmpresaId(personaDTO.getEmpresaId());
        detalle.setTipoPersona(personaDTO.getTipoPersona());
        detalle.setNombreCompleto(personaDTO.getNombreCompleto());
        detalle.setDni(personaDTO.getDni());
        detalle.setFechaNacimiento(personaDTO.getFechaNacimiento());
        detalle.setGenero(personaDTO.getGenero());
        detalle.setTelefono(personaDTO.getTelefono());
        detalle.setCorreo(personaDTO.getCorreo());

        // Información de salud
        saludPersonaRepository.findByPersonaId(id)
                .ifPresent(salud -> detalle.setSalud(saludPersonaMapper.toDTO(salud)));

        // Certificaciones
        detalle.setCertificaciones(
                certificacionMapper.toDTOList(certificacionRepository.findByPersonaId(id))
        );

        return detalle;
    }

    /**
     * Listar personas por empresa
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> listarPorEmpresa(UUID empresaId) {
        List<Persona> personas = personaRepository.findByEmpresaId(empresaId);
        return personaMapper.toDTOList(personas);
    }

    /**
     * Listar personas por tipo
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> listarPorTipo(String tipoPersona) {
        List<Persona> personas = personaRepository.findByTipoPersona(tipoPersona);
        return personaMapper.toDTOList(personas);
    }

    /**
     * Crear nueva persona
     */
    @Transactional
    public PersonaDTO crear(CreatePersonaDTO createPersonaDTO) {
        // Validaciones
        validarDatosPersona(createPersonaDTO.getDni(), createPersonaDTO.getCorreo(), null);

        Persona persona = personaMapper.toEntity(createPersonaDTO);
        persona.setPersonaId(UUID.randomUUID());

        Persona guardada = personaRepository.save(persona);
        return personaMapper.toDTO(guardada);
    }

    /**
     * Actualizar persona existente
     */
    @Transactional
    public PersonaDTO actualizar(UUID id, PersonaDTO personaDTO) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));

        // Validaciones (excluyendo la persona actual)
        validarDatosPersona(personaDTO.getDni(), personaDTO.getCorreo(), id);

        personaMapper.updateEntityFromDTO(personaDTO, persona);

        Persona actualizada = personaRepository.save(persona);
        return personaMapper.toDTO(actualizada);
    }

    /**
     * Eliminar persona y sus datos relacionados
     */
    @Transactional
    public void eliminar(UUID id) {
        if (!personaRepository.existsById(id)) {
            throw new RuntimeException("Persona no encontrada con ID: " + id);
        }

        // Eliminar datos relacionados
        saludPersonaRepository.deleteByPersonaId(id);
        certificacionRepository.deleteByPersonaId(id);

        // Eliminar persona
        personaRepository.deleteById(id);
    }

    /**
     * Validaciones de negocio
     */
    private void validarDatosPersona(String dni, String correo, UUID personaIdExcluir) {
        // Aquí puedes agregar validaciones adicionales:
        // - DNI duplicado
        // - Correo duplicado
        // - Formato de DNI válido
        // - etc.

        if (dni == null || dni.trim().isEmpty()) {
            throw new RuntimeException("El DNI es obligatorio");
        }

        if (dni.length() < 8) {
            throw new RuntimeException("El DNI debe tener al menos 8 caracteres");
        }
    }
}
