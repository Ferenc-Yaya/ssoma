package com.dataservicesperu.ssoma.activos_service.service;

import com.dataservicesperu.ssoma.activos_service.dto.TipoActivoDTO;
import com.dataservicesperu.ssoma.activos_service.entity.TipoActivo;
import com.dataservicesperu.ssoma.activos_service.mapper.ActivoMapper;
import com.dataservicesperu.ssoma.activos_service.repository.TipoActivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoActivoService {

    private final TipoActivoRepository tipoActivoRepository;
    private final ActivoMapper mapper;

    public List<TipoActivoDTO> listarTodos() {
        return tipoActivoRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public TipoActivoDTO obtenerPorId(UUID id) {
        return tipoActivoRepository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public TipoActivoDTO crear(TipoActivoDTO dto) {
        // Validar que no exista el código
        if (dto.getCodigo() != null) {
            // Aquí podrías agregar validación de código duplicado
        }

        TipoActivo entity = mapper.toEntity(dto);
        TipoActivo guardado = tipoActivoRepository.save(entity);
        return mapper.toDTO(guardado);
    }

    public TipoActivoDTO actualizar(UUID id, TipoActivoDTO dto) {
        return tipoActivoRepository.findById(id)
                .map(existente -> {
                    existente.setCodigo(dto.getCodigo());
                    existente.setNombre(dto.getNombre());
                    existente.setDescripcion(dto.getDescripcion());
                    existente.setActivo(dto.getActivo());
                    TipoActivo guardado = tipoActivoRepository.save(existente);
                    return mapper.toDTO(guardado);
                })
                .orElse(null);
    }

    public boolean eliminar(UUID id) {
        if (tipoActivoRepository.existsById(id)) {
            tipoActivoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
