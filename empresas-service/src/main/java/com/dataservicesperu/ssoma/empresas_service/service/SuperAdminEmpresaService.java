package com.dataservicesperu.ssoma.empresas_service.service;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.*;
import com.dataservicesperu.ssoma.empresas_service.mapper.*;
import com.dataservicesperu.ssoma.empresas_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para operaciones de SuperAdmin que cruzan múltiples tenants
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SuperAdminEmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaContactoRepository contactoRepository;
    private final EmpresaServicioRepository servicioRepository;
    private final TipoContratistaRepository tipoContratistaRepository;
    private final EmpresaTipoRepository empresaTipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoCategoriaRepository tipoCategoriaRepository;
    private final EmpresaCategoriaRepository empresaCategoriaRepository;

    private final EmpresaMapper empresaMapper;
    private final ContactoMapper contactoMapper;
    private final ServicioMapper servicioMapper;
    private final TipoContratistaMapper tipoContratistaMapper;
    private final CategoriaMapper categoriaMapper;

    /**
     * Lista todas las empresas de todos los tenants (sin filtro de tenant)
     */
    @Transactional(readOnly = true)
    public List<EmpresaDTO> listarTodasLasEmpresas() {
        log.info("[SUPERADMIN] Listando todas las empresas de todos los tenants");

        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear empresa en un tenant específico (especificando el schema)
     */
    @Transactional
    public EmpresaDTO crearEmpresaEnTenant(CreateEmpresaDTO dto, String tenantId) {
        log.info("[SUPERADMIN] Creando empresa en tenant: {}", tenantId);

        // Aquí deberías cambiar el contexto del tenant antes de crear
        // Esto depende de tu implementación de TenantContext

        // Por ahora, delego al servicio normal
        // TODO: Implementar cambio de tenant context

        throw new UnsupportedOperationException("Implementar cambio de tenant context");
    }

    /**
     * Obtener estadísticas globales de empresas
     */
    @Transactional(readOnly = true)
    public EmpresaEstadisticasDTO obtenerEstadisticasGlobales() {
        log.info("[SUPERADMIN] Obteniendo estadísticas globales");

        long totalEmpresas = empresaRepository.count();
        long empresasActivas = empresaRepository.findByActivoTrue().size();
        long empresasInactivas = totalEmpresas - empresasActivas;

        return new EmpresaEstadisticasDTO(totalEmpresas, empresasActivas, empresasInactivas);
    }

    /**
     * Activar/Desactivar empresa de cualquier tenant
     */
    @Transactional
    public void cambiarEstadoEmpresa(UUID empresaId, boolean activo) {
        log.info("[SUPERADMIN] Cambiando estado de empresa {} a: {}", empresaId, activo);

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        empresa.setActivo(activo);
        empresaRepository.save(empresa);

        log.info("[SUPERADMIN] Estado de empresa actualizado exitosamente");
    }

    /**
     * DTO para estadísticas
     */
    public static class EmpresaEstadisticasDTO {
        private final long totalEmpresas;
        private final long empresasActivas;
        private final long empresasInactivas;

        public EmpresaEstadisticasDTO(long total, long activas, long inactivas) {
            this.totalEmpresas = total;
            this.empresasActivas = activas;
            this.empresasInactivas = inactivas;
        }

        public long getTotalEmpresas() { return totalEmpresas; }
        public long getEmpresasActivas() { return empresasActivas; }
        public long getEmpresasInactivas() { return empresasInactivas; }
    }
}
