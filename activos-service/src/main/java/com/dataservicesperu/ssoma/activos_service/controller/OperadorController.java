package com.dataservicesperu.ssoma.activos_service.controller;

import com.dataservicesperu.ssoma.activos_service.entity.Operador;
import com.dataservicesperu.ssoma.activos_service.repository.OperadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/operadores")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OperadorController {

    private final OperadorRepository operadorRepository;

    @GetMapping
    public ResponseEntity<List<Operador>> listar() {
        return ResponseEntity.ok(operadorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operador> obtener(@PathVariable UUID id) {
        return operadorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Operador> crear(@RequestBody Operador operador) {
        try {
            Operador guardado = operadorRepository.save(operador);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operador> actualizar(@PathVariable UUID id, @RequestBody Operador operador) {
        return operadorRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(operador.getNombre());
                    existente.setDocumento(operador.getDocumento());
                    existente.setActivo(operador.getActivo());
                    Operador guardado = operadorRepository.save(existente);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        if (operadorRepository.existsById(id)) {
            operadorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
