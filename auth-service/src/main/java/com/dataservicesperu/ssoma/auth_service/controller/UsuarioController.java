package com.dataservicesperu.ssoma.auth_service.controller;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;
import com.dataservicesperu.ssoma.auth_service.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioDTO getById(@PathVariable UUID id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO create(@RequestBody UsuarioDTO dto) {
        return usuarioService.create(dto);
    }

    @PutMapping("/{id}")
    public UsuarioDTO update(@PathVariable UUID id, @RequestBody UsuarioDTO dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        usuarioService.delete(id);
    }
}
