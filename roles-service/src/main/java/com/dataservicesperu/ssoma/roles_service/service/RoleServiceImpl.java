package com.dataservicesperu.ssoma.roles_service.service;

import com.dataservicesperu.ssoma.roles_service.dto.RoleDTO;
import com.dataservicesperu.ssoma.roles_service.entity.RoleEntity;
import com.dataservicesperu.ssoma.roles_service.mapper.RoleMapper;
import com.dataservicesperu.ssoma.roles_service.repository.RoleRepository;
import com.ssoma.common.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final TenantService tenantService;

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAllGlobal() {
        return tenantService.executeWithoutTenantFilter(() ->
                roleMapper.toDtoList(roleRepository.findAll())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findById(UUID id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findByCodigoRol(String codigoRol) {
        return roleRepository.findByCodigoRol(codigoRol)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Role not found: " + codigoRol));
    }

    @Override
    @Transactional
    public RoleDTO create(RoleDTO dto) {
        RoleEntity role = roleMapper.toEntity(dto);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO update(UUID id, RoleDTO dto) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));
        roleMapper.updateEntity(dto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }
}