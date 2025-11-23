package com.dataservicesperu.ssoma.activos_service.controller;

import com.dataservicesperu.ssoma.activos_service.dto.TipoActivoDTO;
import com.dataservicesperu.ssoma.activos_service.service.TipoActivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tipos-activo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TipoActivoController {

    private final TipoActivoService tipoActivoService;

    @GetMapping
    public ResponseEntity<List<TipoActivoDTO>> listar() {
        return ResponseEntity.ok(tipoActivoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoActivoDTO> obtener(@PathVariable UUID id) {
        TipoActivoDTO dto = tipoActivoService.obtenerPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TipoActivoDTO> crear(@RequestBody TipoActivoDTO tipoActivo) {
        try {
            TipoActivoDTO guardado = tipoActivoService.crear(tipoActivo);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoActivoDTO> actualizar(@PathVariable UUID id, @RequestBody TipoActivoDTO tipoActivo) {
        TipoActivoDTO actualizado = tipoActivoService.actualizar(id, tipoActivo);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        boolean eliminado = tipoActivoService.eliminar(id);
        return eliminado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
