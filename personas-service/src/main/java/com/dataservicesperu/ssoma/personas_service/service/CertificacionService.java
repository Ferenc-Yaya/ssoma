package com.dataservicesperu.ssoma.personas_service.service;

import com.dataservicesperu.ssoma.personas_service.dto.CertificacionDTO;
import com.dataservicesperu.ssoma.personas_service.entity.Certificacion;
import com.dataservicesperu.ssoma.personas_service.mapper.CertificacionMapper;
import com.dataservicesperu.ssoma.personas_service.repository.CertificacionRepository;
import com.dataservicesperu.ssoma.personas_service.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificacionService {

    private final CertificacionRepository certificacionRepository;
    private final PersonaRepository personaRepository;
    private final CertificacionMapper certificacionMapper;

    /**
     * Listar todas las certificaciones
     */
    @Transactional(readOnly = true)
    public List<CertificacionDTO> listarTodas() {
        List<Certificacion> certificaciones = certificacionRepository.findAll();
        return certificacionMapper.toDTOList(certificaciones);
    }

    /**
     * Obtener certificación por ID
     */
    @Transactional(readOnly = true)
    public CertificacionDTO obtenerPorId(UUID id) {
        Certificacion certificacion = certificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificación no encontrada con ID: " + id));
        return certificacionMapper.toDTO(certificacion);
    }

    /**
     * Listar certificaciones por persona
     */
    @Transactional(readOnly = true)
    public List<CertificacionDTO> listarPorPersona(UUID personaId) {
        List<Certificacion> certificaciones = certificacionRepository.findByPersonaId(personaId);
        return certificacionMapper.toDTOList(certificaciones);
    }

    /**
     * Listar certificaciones vigentes (no vencidas)
     */
    @Transactional(readOnly = true)
    public List<CertificacionDTO> listarCertificacionesVigentes(UUID personaId) {
        List<Certificacion> certificaciones = certificacionRepository.findByPersonaId(personaId);
        LocalDate hoy = LocalDate.now();

        return certificacionMapper.toDTOList(
                certificaciones.stream()
                        .filter(cert -> cert.getFechaVencimiento() == null ||
                                cert.getFechaVencimiento().isAfter(hoy))
                        .toList()
        );
    }

    /**
     * Listar certificaciones próximas a vencer (30 días)
     */
    @Transactional(readOnly = true)
    public List<CertificacionDTO> listarCertificacionesProximasAVencer(UUID personaId) {
        List<Certificacion> certificaciones = certificacionRepository.findByPersonaId(personaId);
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(30);

        return certificacionMapper.toDTOList(
                certificaciones.stream()
                        .filter(cert -> cert.getFechaVencimiento() != null &&
                                cert.getFechaVencimiento().isAfter(hoy) &&
                                cert.getFechaVencimiento().isBefore(fechaLimite))
                        .toList()
        );
    }

    /**
     * Crear nueva certificación
     */
    @Transactional
    public CertificacionDTO crear(CertificacionDTO certificacionDTO) {
        // Validar que la persona existe
        if (!personaRepository.existsById(certificacionDTO.getPersonaId())) {
            throw new RuntimeException("La persona no existe con ID: " + certificacionDTO.getPersonaId());
        }

        // Validaciones de fechas
        validarFechas(certificacionDTO.getFechaEmision(), certificacionDTO.getFechaVencimiento());

        Certificacion certificacion = certificacionMapper.toEntity(certificacionDTO);
        certificacion.setCertificacionId(UUID.randomUUID());

        Certificacion guardada = certificacionRepository.save(certificacion);
        return certificacionMapper.toDTO(guardada);
    }

    /**
     * Actualizar certificación
     */
    @Transactional
    public CertificacionDTO actualizar(UUID id, CertificacionDTO certificacionDTO) {
        Certificacion certificacion = certificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificación no encontrada con ID: " + id));

        // Validaciones de fechas
        validarFechas(certificacionDTO.getFechaEmision(), certificacionDTO.getFechaVencimiento());

        certificacionMapper.updateEntityFromDTO(certificacionDTO, certificacion);

        Certificacion actualizada = certificacionRepository.save(certificacion);
        return certificacionMapper.toDTO(actualizada);
    }

    /**
     * Eliminar certificación
     */
    @Transactional
    public void eliminar(UUID id) {
        if (!certificacionRepository.existsById(id)) {
            throw new RuntimeException("Certificación no encontrada con ID: " + id);
        }
        certificacionRepository.deleteById(id);
    }

    /**
     * Eliminar certificaciones por persona
     */
    @Transactional
    public void eliminarPorPersonaId(UUID personaId) {
        certificacionRepository.deleteByPersonaId(personaId);
    }

    /**
     * Validar que la fecha de vencimiento sea posterior a la fecha de emisión
     */
    private void validarFechas(LocalDate fechaEmision, LocalDate fechaVencimiento) {
        if (fechaEmision == null) {
            throw new RuntimeException("La fecha de emisión es obligatoria");
        }

        if (fechaVencimiento != null && fechaVencimiento.isBefore(fechaEmision)) {
            throw new RuntimeException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }
    }
}
