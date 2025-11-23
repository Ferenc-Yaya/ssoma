package com.dataservicesperu.ssoma.activos_service.controller;

import com.dataservicesperu.ssoma.activos_service.dto.ActivoCreateUpdateDTO;
import com.dataservicesperu.ssoma.activos_service.dto.ActivoDTO;
import com.dataservicesperu.ssoma.activos_service.service.ActivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ActivoController {

    private final ActivoService activoService;

    @GetMapping
    public ResponseEntity<List<ActivoDTO>> listar() {
        return ResponseEntity.ok(activoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivoDTO> obtener(@PathVariable UUID id) {
        ActivoDTO dto = activoService.obtenerPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ActivoDTO>> listarPorCategoria(@PathVariable UUID categoriaId) {
        return ResponseEntity.ok(activoService.listarPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<ActivoDTO> crear(@RequestBody ActivoCreateUpdateDTO activo) {
        try {
            ActivoDTO guardado = activoService.crear(activo);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivoDTO> actualizar(@PathVariable UUID id, @RequestBody ActivoCreateUpdateDTO activo) {
        ActivoDTO actualizado = activoService.actualizar(id, activo);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        boolean eliminado = activoService.eliminar(id);
        return eliminado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<ActivoDTO> toggleActivo(@PathVariable UUID id) {
        ActivoDTO actualizado = activoService.toggleActivo(id);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/validar-requisitos")
    public ResponseEntity<Boolean> validarRequisitos(@PathVariable UUID id) {
        boolean cumple = activoService.validarCumplimientoRequisitos(id);
        return ResponseEntity.ok(cumple);
    }
}
