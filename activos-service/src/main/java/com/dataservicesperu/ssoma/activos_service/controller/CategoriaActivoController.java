package com.dataservicesperu.ssoma.activos_service.controller;

import com.dataservicesperu.ssoma.activos_service.entity.CategoriaActivo;
import com.dataservicesperu.ssoma.activos_service.repository.CategoriaActivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias-activo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaActivoController {

    private final CategoriaActivoRepository categoriaActivoRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaActivo>> listar() {
        return ResponseEntity.ok(categoriaActivoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaActivo> obtener(@PathVariable UUID id) {
        return categoriaActivoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<CategoriaActivo>> listarPorTipo(@PathVariable UUID tipoId) {
        return ResponseEntity.ok(categoriaActivoRepository.findByTipoId(tipoId));
    }

    @PostMapping
    public ResponseEntity<CategoriaActivo> crear(@RequestBody CategoriaActivo categoria) {
        try {
            CategoriaActivo guardado = categoriaActivoRepository.save(categoria);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaActivo> actualizar(@PathVariable UUID id, @RequestBody CategoriaActivo categoria) {
        return categoriaActivoRepository.findById(id)
                .map(existente -> {
                    existente.setTipoId(categoria.getTipoId());
                    existente.setCodigo(categoria.getCodigo());
                    existente.setNombre(categoria.getNombre());
                    existente.setDescripcion(categoria.getDescripcion());
                    existente.setActivo(categoria.getActivo());
                    CategoriaActivo guardado = categoriaActivoRepository.save(existente);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        if (categoriaActivoRepository.existsById(id)) {
            categoriaActivoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
