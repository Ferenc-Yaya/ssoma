package com.dataservicesperu.ssoma.roles_service.controller;

import com.dataservicesperu.ssoma.roles_service.dto.RoleDTO;
import com.dataservicesperu.ssoma.roles_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleDTO getById(@PathVariable UUID id) {
        return roleService.findById(id);
    }

    @GetMapping("/codigo/{codigoRol}")
    public RoleDTO getByCodigoRol(@PathVariable String codigoRol) {
        return roleService.findByCodigoRol(codigoRol);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO create(@RequestBody RoleDTO request) {
        return roleService.create(request);
    }

    @PutMapping("/{id}")
    public RoleDTO update(@PathVariable UUID id, @RequestBody RoleDTO request) {
        return roleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        roleService.delete(id);
    }
}