package com.dataservicesperu.ssoma.personas_service.controller;

import com.dataservicesperu.ssoma.personas_service.dto.CertificacionDTO;
import com.dataservicesperu.ssoma.personas_service.service.CertificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/certificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CertificacionController {

    private final CertificacionService certificacionService;

    @GetMapping
    public ResponseEntity<List<CertificacionDTO>> listarTodas() {
        List<CertificacionDTO> certificaciones = certificacionService.listarTodas();
        return ResponseEntity.ok(certificaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificacionDTO> obtenerPorId(@PathVariable UUID id) {
        try {
            CertificacionDTO certificacion = certificacionService.obtenerPorId(id);
            return ResponseEntity.ok(certificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<CertificacionDTO>> listarPorPersona(@PathVariable UUID personaId) {
        List<CertificacionDTO> certificaciones = certificacionService.listarPorPersona(personaId);
        return ResponseEntity.ok(certificaciones);
    }

    @GetMapping("/persona/{personaId}/vigentes")
    public ResponseEntity<List<CertificacionDTO>> listarCertificacionesVigentes(@PathVariable UUID personaId) {
        List<CertificacionDTO> certificaciones = certificacionService.listarCertificacionesVigentes(personaId);
        return ResponseEntity.ok(certificaciones);
    }

    @GetMapping("/persona/{personaId}/proximas-vencer")
    public ResponseEntity<List<CertificacionDTO>> listarCertificacionesProximasAVencer(@PathVariable UUID personaId) {
        List<CertificacionDTO> certificaciones = certificacionService.listarCertificacionesProximasAVencer(personaId);
        return ResponseEntity.ok(certificaciones);
    }

    @PostMapping
    public ResponseEntity<CertificacionDTO> crear(@RequestBody CertificacionDTO certificacionDTO) {
        try {
            CertificacionDTO certificacion = certificacionService.crear(certificacionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(certificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificacionDTO> actualizar(@PathVariable UUID id, @RequestBody CertificacionDTO certificacionDTO) {
        try {
            CertificacionDTO actualizada = certificacionService.actualizar(id, certificacionDTO);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            certificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/persona/{personaId}")
    public ResponseEntity<Void> eliminarPorPersonaId(@PathVariable UUID personaId) {
        certificacionService.eliminarPorPersonaId(personaId);
        return ResponseEntity.noContent().build();
    }
}
