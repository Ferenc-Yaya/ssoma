package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.RolDTO;
import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import com.dataservicesperu.ssoma.auth_service.mapper.RolMapper;
import com.dataservicesperu.ssoma.auth_service.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> findAll() {
        return rolMapper.toDtoList(rolRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO findById(UUID id) {
        return rolRepository.findById(id)
                .map(rolMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Rol not found: " + id));
    }

    @Override
    @Transactional
    public RolDTO create(RolDTO dto) {
        RolEntity rol = rolMapper.toEntity(dto);
        return rolMapper.toDto(rolRepository.save(rol));
    }

    @Override
    @Transactional
    public RolDTO update(UUID id, RolDTO dto) {
        RolEntity rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol not found: " + id));
        rolMapper.updateEntity(dto, rol);
        return rolMapper.toDto(rolRepository.save(rol));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        rolRepository.deleteById(id);
    }
}