package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import com.dataservicesperu.ssoma.auth_service.mapper.UsuarioMapper;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        return usuarioMapper.toDtoList(usuarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO findById(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Usuario not found: " + id));
    }

    @Override
    @Transactional
    public UsuarioDTO create(UsuarioDTO dto) {
        UsuarioEntity usuario = usuarioMapper.toEntity(dto);
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioDTO update(UUID id, UsuarioDTO dto) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found: " + id));
        usuarioMapper.updateEntity(dto, usuario);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        usuarioRepository.deleteById(id);
    }
}