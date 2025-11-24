package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;
import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import com.dataservicesperu.ssoma.auth_service.mapper.UsuarioMapper;
import com.dataservicesperu.ssoma.auth_service.repository.RolRepository;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO findById(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
    }

    @Override
    public UsuarioDTO create(UsuarioDTO dto) {
        RolEntity rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + dto.getRolId()));

        UsuarioEntity entity = usuarioMapper.toEntity(dto);
        entity.setRol(rol);
        entity.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        
        UsuarioEntity saved = usuarioRepository.save(entity);
        return usuarioMapper.toDto(saved);
    }

    @Override
    public UsuarioDTO update(UUID id, UsuarioDTO dto) {
        UsuarioEntity existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));

        if (dto.getRolId() != null && !dto.getRolId().equals(existing.getRol().getRolId())) {
            RolEntity nuevoRol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + dto.getRolId()));
            existing.setRol(nuevoRol);
        }

        if (dto.getTenantId() != null) existing.setTenantId(dto.getTenantId());
        if (dto.getPersonaId() != null) existing.setPersonaId(dto.getPersonaId());
        if (dto.getEmpresaId() != null) existing.setEmpresaId(dto.getEmpresaId());
        if (dto.getEsHost() != null) existing.setEsHost(dto.getEsHost());
        if (dto.getUsername() != null) existing.setUsername(dto.getUsername());
        if (dto.getActivo() != null) existing.setActivo(dto.getActivo());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        UsuarioEntity saved = usuarioRepository.save(existing);
        return usuarioMapper.toDto(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
