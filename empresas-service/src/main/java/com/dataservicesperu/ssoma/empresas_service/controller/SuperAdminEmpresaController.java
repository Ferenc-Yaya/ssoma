package com.dataservicesperu.ssoma.empresas_service.controller;

import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.service.SuperAdminEmpresaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller exclusivo para operaciones de SuperAdmin
 * Requiere rol SUPER_ADMIN
 */
@RestController
@RequestMapping("/api/superadmin/empresas")
@RequiredArgsConstructor
@Slf4j
public class SuperAdminEmpresaController {

    private final SuperAdminEmpresaService superAdminService;

    @GetMapping("/todas")
    public ResponseEntity<List<EmpresaDTO>> listarTodasLasEmpresas() {
        log.info("[SUPERADMIN] GET /api/superadmin/empresas/todas");
        List<EmpresaDTO> empresas = superAdminService.listarTodasLasEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<SuperAdminEmpresaService.EmpresaEstadisticasDTO> obtenerEstadisticas() {
        log.info("[SUPERADMIN] GET /api/superadmin/empresas/estadisticas");
        SuperAdminEmpresaService.EmpresaEstadisticasDTO stats =
                superAdminService.obtenerEstadisticasGlobales();
        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/{empresaId}/estado")
    public ResponseEntity<Void> cambiarEstadoEmpresa(
            @PathVariable UUID empresaId,
            @RequestParam boolean activo) {
        log.info("[SUPERADMIN] PATCH /api/superadmin/empresas/{}/estado - Activo: {}",
                empresaId, activo);
        try {
            superAdminService.cambiarEstadoEmpresa(empresaId, activo);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
