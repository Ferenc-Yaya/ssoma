package com.dataservicesperu.ssoma.roles_service.service;

import com.dataservicesperu.ssoma.roles_service.dto.RoleDTO;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<RoleDTO> findAll();

    List<RoleDTO> findAllGlobal();

    RoleDTO findById(UUID id);

    RoleDTO findByCodigoRol(String codigoRol);

    RoleDTO create(RoleDTO dto);

    RoleDTO update(UUID id, RoleDTO dto);

    void delete(UUID id);
}