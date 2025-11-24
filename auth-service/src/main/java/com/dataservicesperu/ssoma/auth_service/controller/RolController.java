package com.dataservicesperu.ssoma.auth_service.controller;

import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import com.dataservicesperu.ssoma.auth_service.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    // Roles protegidos del sistema - no se pueden modificar ni eliminar
    private static final Set<String> ROLES_PROTEGIDOS = Set.of(
            "SUPER_ADMIN", "ADMIN_HOST", "ADMIN_PROVEEDOR"
    );

    @GetMapping
    public List<RolEntity> getAll() {
        return rolRepository.findAll();
    }

    @GetMapping("/{id}")
    public RolEntity getById(@PathVariable UUID id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolEntity create(@RequestBody RolEntity rol) {
        // Verificar que no use código de rol protegido
        if (ROLES_PROTEGIDOS.contains(rol.getCodigoRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede crear un rol con código protegido: " + rol.getCodigoRol());
        }

        // Verificar que no exista el código
        if (rolRepository.findByCodigoRol(rol.getCodigoRol()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un rol con código: " + rol.getCodigoRol());
        }

        return rolRepository.save(rol);
    }

    @PutMapping("/{id}")
    public RolEntity update(@PathVariable UUID id, @RequestBody RolEntity rolActualizado) {
        RolEntity existing = rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        // No permitir modificar roles protegidos
        if (ROLES_PROTEGIDOS.contains(existing.getCodigoRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No se puede modificar el rol del sistema: " + existing.getCodigoRol());
        }

        // No permitir cambiar a código protegido
        if (ROLES_PROTEGIDOS.contains(rolActualizado.getCodigoRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede usar código de rol protegido: " + rolActualizado.getCodigoRol());
        }

        existing.setCodigoRol(rolActualizado.getCodigoRol());
        existing.setNombreMostrar(rolActualizado.getNombreMostrar());

        return rolRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        RolEntity existing = rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        // No permitir eliminar roles protegidos
        if (ROLES_PROTEGIDOS.contains(existing.getCodigoRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No se puede eliminar el rol del sistema: " + existing.getCodigoRol());
        }

        rolRepository.deleteById(id);
    }
}