package com.dataservicesperu.ssoma.roles_service.controller;

import com.dataservicesperu.ssoma.roles_service.dto.RoleDTO;
import com.dataservicesperu.ssoma.roles_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleAdminController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAllGlobal() {
        return roleService.findAllGlobal();
    }
}