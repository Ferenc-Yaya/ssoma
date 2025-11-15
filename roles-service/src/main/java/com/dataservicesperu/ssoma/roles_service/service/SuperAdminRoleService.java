package com.dataservicesperu.ssoma.roles_service.service;

import com.dataservicesperu.ssoma.roles_service.config.CurrentTenantHolder;
import com.dataservicesperu.ssoma.roles_service.entity.Role;
import com.dataservicesperu.ssoma.roles_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminRoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Obtiene los roles de todos los tenants disponibles
     */
    public List<Role> getAllRolesFromAllTenants() {
        List<Role> allRoles = new ArrayList<>();

        // Guarda el tenant actual para restaurarlo al final
        String currentTenant = CurrentTenantHolder.getTenantId();

        try {
            // Obtener roles de tenant_a
            CurrentTenantHolder.setTenantId("tenant_a");
            allRoles.addAll(roleRepository.findAll());

            // Obtener roles de tenant_b
            CurrentTenantHolder.setTenantId("tenant_b");
            allRoles.addAll(roleRepository.findAll());

            // Opcional: obtener roles del schema public si es necesario
            // CurrentTenantHolder.setTenantId("public");
            // allRoles.addAll(roleRepository.findAll());

        } finally {
            // Restaura el tenant original
            CurrentTenantHolder.setTenantId(currentTenant);
        }

        return allRoles;
    }
}
