package com.dataservicesperu.ssoma.personas_service.controller;

import com.dataservicesperu.ssoma.personas_service.dto.CreatePersonaDTO;
import com.dataservicesperu.ssoma.personas_service.dto.PersonaDTO;
import com.dataservicesperu.ssoma.personas_service.dto.PersonaDetalleDTO;
import com.dataservicesperu.ssoma.personas_service.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listarTodas() {
        List<PersonaDTO> personas = personaService.listarTodas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtenerPorId(@PathVariable UUID id) {
        try {
            PersonaDTO persona = personaService.obtenerPorId(id);
            return ResponseEntity.ok(persona);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<PersonaDetalleDTO> obtenerDetallePorId(@PathVariable UUID id) {
        try {
            PersonaDetalleDTO detalle = personaService.obtenerDetallePorId(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<PersonaDTO>> listarPorEmpresa(@PathVariable UUID empresaId) {
        List<PersonaDTO> personas = personaService.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/tipo/{tipoPersona}")
    public ResponseEntity<List<PersonaDTO>> listarPorTipo(@PathVariable String tipoPersona) {
        List<PersonaDTO> personas = personaService.listarPorTipo(tipoPersona);
        return ResponseEntity.ok(personas);
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> crear(@RequestBody CreatePersonaDTO createPersonaDTO) {
        try {
            PersonaDTO persona = personaService.crear(createPersonaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(persona);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> actualizar(@PathVariable UUID id, @RequestBody PersonaDTO personaDTO) {
        try {
            PersonaDTO actualizada = personaService.actualizar(id, personaDTO);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            personaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
