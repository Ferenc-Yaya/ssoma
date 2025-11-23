package com.dataservicesperu.ssoma.activos_service.service;

import com.dataservicesperu.ssoma.activos_service.dto.ActivoCreateUpdateDTO;
import com.dataservicesperu.ssoma.activos_service.dto.ActivoDTO;
import com.dataservicesperu.ssoma.activos_service.entity.Activo;
import com.dataservicesperu.ssoma.activos_service.entity.ActivoRequisito;
import com.dataservicesperu.ssoma.activos_service.entity.Operador;
import com.dataservicesperu.ssoma.activos_service.mapper.ActivoMapper;
import com.dataservicesperu.ssoma.activos_service.repository.ActivoRepository;
import com.dataservicesperu.ssoma.activos_service.repository.ActivoRequisitoRepository;
import com.dataservicesperu.ssoma.activos_service.repository.OperadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivoService {

    private final ActivoRepository activoRepository;
    private final ActivoRequisitoRepository activoRequisitoRepository;
    private final OperadorRepository operadorRepository;
    private final ActivoMapper mapper;

    public List<ActivoDTO> listarTodos() {
        return activoRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActivoDTO obtenerPorId(UUID id) {
        return activoRepository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public List<ActivoDTO> listarPorCategoria(UUID categoriaId) {
        return activoRepository.findByCategoriaId(categoriaId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActivoDTO crear(ActivoCreateUpdateDTO dto) {
        // Crear el activo
        Activo activo = new Activo();
        activo.setCategoriaId(dto.getCategoriaId());
        activo.setCodigo(dto.getCodigo());
        activo.setNombre(dto.getNombre());
        activo.setDescripcion(dto.getDescripcion());
        activo.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        // Asignar operadores si existen
        if (dto.getOperadorIds() != null && !dto.getOperadorIds().isEmpty()) {
            List<Operador> operadores = operadorRepository.findAllById(dto.getOperadorIds());
            activo.setOperadores(operadores);
        }

        Activo guardado = activoRepository.save(activo);

        // Crear los requisitos asociados
        if (dto.getRequisitos() != null && !dto.getRequisitos().isEmpty()) {
            List<ActivoRequisito> requisitos = new ArrayList<>();
            for (ActivoCreateUpdateDTO.RequisitoCreateDTO req : dto.getRequisitos()) {
                ActivoRequisito activoRequisito = new ActivoRequisito();
                activoRequisito.setActivoId(guardado.getActivoId());
                activoRequisito.setRequisitoId(req.getRequisitoId());
                activoRequisito.setCumple(req.getCumple());
                activoRequisito.setEvidencia(req.getEvidencia());
                requisitos.add(activoRequisito);
            }
            activoRequisitoRepository.saveAll(requisitos);
        }

        // Recargar el activo con todas sus relaciones
        return activoRepository.findById(guardado.getActivoId())
                .map(mapper::toDTO)
                .orElse(null);
    }

    public ActivoDTO actualizar(UUID id, ActivoCreateUpdateDTO dto) {
        return activoRepository.findById(id)
                .map(existente -> {
                    existente.setCategoriaId(dto.getCategoriaId());
                    existente.setCodigo(dto.getCodigo());
                    existente.setNombre(dto.getNombre());
                    existente.setDescripcion(dto.getDescripcion());
                    existente.setActivo(dto.getActivo());

                    // Actualizar operadores
                    if (dto.getOperadorIds() != null) {
                        List<Operador> operadores = operadorRepository.findAllById(dto.getOperadorIds());
                        existente.setOperadores(operadores);
                    }

                    Activo guardado = activoRepository.save(existente);

                    // Actualizar requisitos (eliminar existentes y crear nuevos)
                    if (dto.getRequisitos() != null) {
                        // Eliminar requisitos existentes
                        activoRequisitoRepository.deleteAll(
                            activoRequisitoRepository.findByActivoId(id)
                        );

                        // Crear nuevos requisitos
                        List<ActivoRequisito> requisitos = new ArrayList<>();
                        for (ActivoCreateUpdateDTO.RequisitoCreateDTO req : dto.getRequisitos()) {
                            ActivoRequisito activoRequisito = new ActivoRequisito();
                            activoRequisito.setActivoId(id);
                            activoRequisito.setRequisitoId(req.getRequisitoId());
                            activoRequisito.setCumple(req.getCumple());
                            activoRequisito.setEvidencia(req.getEvidencia());
                            requisitos.add(activoRequisito);
                        }
                        activoRequisitoRepository.saveAll(requisitos);
                    }

                    // Recargar con todas las relaciones
                    return activoRepository.findById(guardado.getActivoId())
                            .map(mapper::toDTO)
                            .orElse(null);
                })
                .orElse(null);
    }

    public boolean eliminar(UUID id) {
        if (activoRepository.existsById(id)) {
            activoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ActivoDTO toggleActivo(UUID id) {
        return activoRepository.findById(id)
                .map(activo -> {
                    activo.setActivo(!activo.getActivo());
                    Activo guardado = activoRepository.save(activo);
                    return mapper.toDTO(guardado);
                })
                .orElse(null);
    }

    /**
     * Valida si un activo cumple con todos sus requisitos obligatorios
     */
    public boolean validarCumplimientoRequisitos(UUID activoId) {
        Activo activo = activoRepository.findById(activoId).orElse(null);
        if (activo == null) return false;

        List<ActivoRequisito> requisitos = activoRequisitoRepository.findByActivoId(activoId);

        // Verificar que todos los requisitos obligatorios cumplan
        for (ActivoRequisito ar : requisitos) {
            if (ar.getRequisito() != null && 
                Boolean.TRUE.equals(ar.getRequisito().getObligatorio()) && 
                !Boolean.TRUE.equals(ar.getCumple())) {
                return false;
            }
        }

        return true;
    }
}
