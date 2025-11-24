package com.dataservicesperu.ssoma.empresas_service.controller;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.TipoContratistaDTO;
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
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al crear empresa: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/tipos-contratista")
    public ResponseEntity<List<TipoContratistaDTO>> listarTiposContratista() {
        log.info("GET /empresas/tipos-contratista - Listar tipos");
        List<TipoContratistaDTO> tipos = empresaService.listarTiposContratista();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/host")
    public ResponseEntity<EmpresaDTO> obtenerEmpresaHost() {
        log.info("GET /empresas/host - Obtener empresa host");
        try {
            EmpresaDTO empresa = empresaService.obtenerEmpresaHost();
            return ResponseEntity.ok(empresa);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al obtener empresa host: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        log.info("GET /empresas - Listar todas las empresas activas");
        try {
            List<EmpresaDTO> empresas = empresaService.listarEmpresasActivas();
            return ResponseEntity.ok(empresas);
        } catch (IllegalStateException e) {
            log.error("Error al listar empresas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Este debe ir DESPUÉS de los endpoints específicos
    @GetMapping("/{empresaId}")
    public ResponseEntity<EmpresaDTO> obtenerEmpresa(@PathVariable UUID empresaId) {
        log.info("GET /empresas/{} - Obtener empresa", empresaId);
        try {
            EmpresaDTO empresa = empresaService.obtenerEmpresaPorId(empresaId);
            return ResponseEntity.ok(empresa);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al obtener empresa: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{empresaId}")
    public ResponseEntity<EmpresaDTO> actualizarEmpresa(
            @PathVariable UUID empresaId,
            @Valid @RequestBody CreateEmpresaDTO dto) {
        log.info("PUT /empresas/{} - Actualizar empresa", empresaId);
        try {
            EmpresaDTO empresaActualizada = empresaService.actualizarEmpresa(empresaId, dto);
            return ResponseEntity.ok(empresaActualizada);
        } catch (IllegalArgumentException | IllegalStateException e) {
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
        } catch (IllegalArgumentException | IllegalStateException e) {
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
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
