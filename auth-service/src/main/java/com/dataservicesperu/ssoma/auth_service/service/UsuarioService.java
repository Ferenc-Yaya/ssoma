package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(UUID id);
    UsuarioDTO create(UsuarioDTO dto);
    UsuarioDTO update(UUID id, UsuarioDTO dto);
    void delete(UUID id);
}
