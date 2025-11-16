package com.dataservicesperu.ssoma.personas_service.controller;

import com.dataservicesperu.ssoma.personas_service.dto.SaludPersonaDTO;
import com.dataservicesperu.ssoma.personas_service.service.SaludPersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/salud")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SaludPersonaController {

    private final SaludPersonaService saludPersonaService;

    @GetMapping
    public ResponseEntity<List<SaludPersonaDTO>> listarTodas() {
        List<SaludPersonaDTO> registros = saludPersonaService.listarTodas();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaludPersonaDTO> obtenerPorId(@PathVariable UUID id) {
        try {
            SaludPersonaDTO salud = saludPersonaService.obtenerPorId(id);
            return ResponseEntity.ok(salud);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<SaludPersonaDTO> obtenerPorPersonaId(@PathVariable UUID personaId) {
        try {
            SaludPersonaDTO salud = saludPersonaService.obtenerPorPersonaId(personaId);
            return ResponseEntity.ok(salud);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SaludPersonaDTO> crear(@RequestBody SaludPersonaDTO saludPersonaDTO) {
        try {
            SaludPersonaDTO salud = saludPersonaService.crear(saludPersonaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salud);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaludPersonaDTO> actualizar(@PathVariable UUID id, @RequestBody SaludPersonaDTO saludPersonaDTO) {
        try {
            SaludPersonaDTO actualizada = saludPersonaService.actualizar(id, saludPersonaDTO);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            saludPersonaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/persona/{personaId}")
    public ResponseEntity<Void> eliminarPorPersonaId(@PathVariable UUID personaId) {
        saludPersonaService.eliminarPorPersonaId(personaId);
        return ResponseEntity.noContent().build();
    }
}
