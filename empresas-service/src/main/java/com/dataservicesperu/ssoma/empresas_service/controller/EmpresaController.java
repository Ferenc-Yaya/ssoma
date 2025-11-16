package com.dataservicesperu.ssoma.empresas_service.controller;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.TipoEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
@Slf4j
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaDTO> crearEmpresa(@Valid @RequestBody CreateEmpresaDTO dto) {
        log.info("POST /empresas - Crear empresa con RUC: {}", dto.getRuc());
        try {
            EmpresaDTO empresaCreada = empresaService.crearEmpresa(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresaCreada);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear empresa: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<EmpresaDTO> obtenerEmpresa(@PathVariable UUID empresaId) {
        log.info("GET /empresas/{} - Obtener empresa", empresaId);
        try {
            EmpresaDTO empresa = empresaService.obtenerEmpresaPorId(empresaId);
            return ResponseEntity.ok(empresa);
        } catch (IllegalArgumentException e) {
            log.error("Error al obtener empresa: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        log.info("GET /empresas - Listar todas las empresas activas");
        List<EmpresaDTO> empresas = empresaService.listarEmpresasActivas();
        return ResponseEntity.ok(empresas);
    }

    @PutMapping("/{empresaId}")
    public ResponseEntity<EmpresaDTO> actualizarEmpresa(
            @PathVariable UUID empresaId,
            @Valid @RequestBody CreateEmpresaDTO dto) {
        log.info("PUT /empresas/{} - Actualizar empresa", empresaId);
        try {
            EmpresaDTO empresaActualizada = empresaService.actualizarEmpresa(empresaId, dto);
            return ResponseEntity.ok(empresaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar empresa: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{empresaId}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable UUID empresaId) {
        log.info("DELETE /empresas/{} - Eliminar empresa", empresaId);
        try {
            empresaService.eliminarEmpresa(empresaId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar empresa: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{empresaId}/toggle-activo")
    public ResponseEntity<Void> toggleActivo(@PathVariable UUID empresaId) {
        log.info("PATCH /empresas/{}/toggle-activo - Cambiar estado", empresaId);
        try {
            empresaService.toggleActivo(empresaId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipos-empresa")
    public ResponseEntity<List<TipoEmpresaDTO>> listarTiposEmpresa() {
        log.info("GET /empresas/tipos-empresa - Listar tipos");
        List<TipoEmpresaDTO> tipos = empresaService.listarTiposEmpresa();
        return ResponseEntity.ok(tipos);
    }
}
