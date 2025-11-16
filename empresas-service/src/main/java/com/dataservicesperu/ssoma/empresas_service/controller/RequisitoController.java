package com.dataservicesperu.ssoma.empresas_service.controller;

import com.dataservicesperu.ssoma.empresas_service.dto.RequisitoDTO;
import com.dataservicesperu.ssoma.empresas_service.service.RequisitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas/{empresaId}/tipos/{tipoId}/requisitos")
@RequiredArgsConstructor
@Slf4j
public class RequisitoController {

    private final RequisitoService requisitoService;

    @GetMapping
    public ResponseEntity<List<RequisitoDTO>> obtenerRequisitos(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId) {
        log.info("GET /api/empresas/{}/tipos/{}/requisitos", empresaId, tipoId);

        try {
            List<RequisitoDTO> requisitos = requisitoService.obtenerRequisitosAplicables(empresaId, tipoId);
            return ResponseEntity.ok(requisitos);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{requisitoId}/desactivar")
    public ResponseEntity<Void> desactivarRequisito(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId,
            @PathVariable UUID requisitoId) {
        log.info("PATCH /api/empresas/{}/tipos/{}/requisitos/{}/desactivar",
                empresaId, tipoId, requisitoId);

        try {
            requisitoService.desactivarRequisitoParaEmpresa(empresaId, tipoId, requisitoId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{requisitoId}/activar")
    public ResponseEntity<Void> activarRequisito(
            @PathVariable UUID empresaId,
            @PathVariable UUID tipoId,
            @PathVariable UUID requisitoId) {
        log.info("PATCH /api/empresas/{}/tipos/{}/requisitos/{}/activar",
                empresaId, tipoId, requisitoId);

        try {
            requisitoService.activarRequisitoParaEmpresa(empresaId, tipoId, requisitoId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
