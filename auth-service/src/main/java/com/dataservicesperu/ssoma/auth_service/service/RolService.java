package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.RolDTO;

import java.util.List;
import java.util.UUID;

public interface RolService {
    List<RolDTO> findAll();
    RolDTO findById(UUID id);
    RolDTO create(RolDTO dto);
    RolDTO update(UUID id, RolDTO dto);
    void delete(UUID id);
}
