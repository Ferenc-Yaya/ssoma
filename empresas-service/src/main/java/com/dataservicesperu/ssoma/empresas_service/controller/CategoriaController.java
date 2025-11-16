package com.dataservicesperu.ssoma.empresas_service.controller;

import com.dataservicesperu.ssoma.empresas_service.dto.CategoriaDTO;
import com.dataservicesperu.ssoma.empresas_service.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas/{empresaId}/tipos/{tipoId}/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId) {
        log.info("GET /api/empresas/{}/tipos/{}/categorias", empresaId, tipoId);

        try {
            List<CategoriaDTO> categorias = categoriaService.obtenerCategoriasAplicables(empresaId, tipoId);
            return ResponseEntity.ok(categorias);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{categoriaId}/desactivar")
    public ResponseEntity<Void> desactivarCategoria(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId,
            @PathVariable UUID categoriaId) {
        log.info("PATCH /api/empresas/{}/tipos/{}/categorias/{}/desactivar",
                empresaId, tipoId, categoriaId);

        try {
            categoriaService.desactivarCategoriaParaEmpresa(empresaId, tipoId, categoriaId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{categoriaId}/activar")
    public ResponseEntity<Void> activarCategoria(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId,
            @PathVariable UUID categoriaId) {
        log.info("PATCH /api/empresas/{}/tipos/{}/categorias/{}/activar",
                empresaId, tipoId, categoriaId);

        try {
            categoriaService.activarCategoriaParaEmpresa(empresaId, tipoId, categoriaId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
