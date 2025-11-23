package com.dataservicesperu.ssoma.activos_service.controller;

import com.dataservicesperu.ssoma.activos_service.entity.Requisito;
import com.dataservicesperu.ssoma.activos_service.repository.RequisitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/requisitos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RequisitoController {

    private final RequisitoRepository requisitoRepository;

    @GetMapping
    public ResponseEntity<List<Requisito>> listar() {
        return ResponseEntity.ok(requisitoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Requisito> obtener(@PathVariable UUID id) {
        return requisitoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Requisito>> listarPorCategoria(@PathVariable UUID categoriaId) {
        return ResponseEntity.ok(requisitoRepository.findByCategoriaId(categoriaId));
    }

    @PostMapping
    public ResponseEntity<Requisito> crear(@RequestBody Requisito requisito) {
        try {
            Requisito guardado = requisitoRepository.save(requisito);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Requisito> actualizar(@PathVariable UUID id, @RequestBody Requisito requisito) {
        return requisitoRepository.findById(id)
                .map(existente -> {
                    existente.setCategoriaId(requisito.getCategoriaId());
                    existente.setCodigo(requisito.getCodigo());
                    existente.setNombre(requisito.getNombre());
                    existente.setDescripcion(requisito.getDescripcion());
                    existente.setObligatorio(requisito.getObligatorio());
                    existente.setActivo(requisito.getActivo());
                    Requisito guardado = requisitoRepository.save(existente);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        if (requisitoRepository.existsById(id)) {
            requisitoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
