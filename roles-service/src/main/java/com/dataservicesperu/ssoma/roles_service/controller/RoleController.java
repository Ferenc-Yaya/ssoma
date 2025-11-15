package com.dataservicesperu.ssoma.roles_service.controller;

import com.dataservicesperu.ssoma.roles_service.config.CurrentTenantHolder;
import com.dataservicesperu.ssoma.roles_service.entity.Role;
import com.dataservicesperu.ssoma.roles_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.dataservicesperu.ssoma.roles_service.service.SuperAdminRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SuperAdminRoleService superAdminRoleService;

    @GetMapping
    public List<Role> getAllRoles() {
        // Si es superadmin, obtener roles de todos los schemas
        if (CurrentTenantHolder.isSuperAdmin()) {
            return superAdminRoleService.getAllRolesFromAllTenants();
        }
        // Si no, obtener solo los del tenant actual
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
        return roleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody Role roleDetails) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setNombreRol(roleDetails.getNombreRol());
                    role.setDescripcion(roleDetails.getDescripcion());
                    return ResponseEntity.ok(roleRepository.save(role));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
