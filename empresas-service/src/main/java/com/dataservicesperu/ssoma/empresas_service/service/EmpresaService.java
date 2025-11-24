package com.dataservicesperu.ssoma.empresas_service.service;

import com.dataservicesperu.ssoma.common.context.TenantContext;
import com.dataservicesperu.ssoma.empresas_service.dto.*;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaContactoRepository contactoRepository;
    private final TipoContratistaRepository tipoContratistaRepository;

    private final EmpresaMapper empresaMapper;
    private final ContactoMapper contactoMapper;
    private final TipoContratistaMapper tipoContratistaMapper;

    @Transactional
    public EmpresaDTO crearEmpresa(CreateEmpresaDTO dto) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Creando empresa con RUC: {} para tenant: {}", dto.getRuc(), tenantId);

        if (empresaRepository.existsByTenantIdAndRuc(tenantId, dto.getRuc())) {
            throw new IllegalArgumentException("Ya existe una empresa con el RUC: " + dto.getRuc());
        }

        // Verificar que el tipo existe
        TipoContratista tipo = tipoContratistaRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado: " + dto.getTipoId()));

        // Crear la entidad empresa
        Empresa empresa = empresaMapper.toEntity(dto);
        empresa.setTenantId(tenantId);
        empresa.setTipo(tipo);

        // Agregar contactos
        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContacto contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setTenantId(tenantId);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        // Guardar empresa
        empresaRepository.save(empresa);

        log.info("Empresa creada exitosamente con ID: {}", empresa.getEmpresaId());
        return obtenerEmpresaPorId(empresa.getEmpresaId());
    }

    @Transactional(readOnly = true)
    public EmpresaDTO obtenerEmpresaPorId(UUID empresaId) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Obteniendo empresa con ID: {} para tenant: {}", empresaId, tenantId);

        Empresa empresa = empresaRepository.findByIdAndTenantIdWithDetails(empresaId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        return empresaMapper.toDTO(empresa);
    }

    @Transactional(readOnly = true)
    public List<EmpresaDTO> listarEmpresasActivas() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Listando todas las empresas activas para tenant: {}", tenantId);

        List<Empresa> empresas = empresaRepository.findByTenantIdAndActivoTrue(tenantId);
        return empresas.stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmpresaDTO actualizarEmpresa(UUID empresaId, CreateEmpresaDTO dto) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Actualizando empresa con ID: {} para tenant: {}", empresaId, tenantId);

        Empresa empresa = empresaRepository.findByIdAndTenantIdWithDetails(empresaId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        // Actualizar datos básicos
        empresaMapper.updateEntityFromDTO(dto, empresa);

        // Actualizar tipo si cambió
        if (dto.getTipoId() != null && 
            (empresa.getTipo() == null || !empresa.getTipo().getTipoId().equals(dto.getTipoId()))) {
            TipoContratista tipo = tipoContratistaRepository.findById(dto.getTipoId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado: " + dto.getTipoId()));
            empresa.setTipo(tipo);
        }

        // === ACTUALIZAR CONTACTOS ===
        List<EmpresaContacto> contactosExistentes = contactoRepository
                .findByEmpresa_EmpresaIdAndTenantId(empresaId, tenantId);
        contactoRepository.deleteAll(contactosExistentes);
        empresa.getContactos().clear();

        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContacto contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setTenantId(tenantId);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        empresaRepository.save(empresa);

        log.info("Empresa actualizada exitosamente: {}", empresaId);
        return obtenerEmpresaPorId(empresaId);
    }

    @Transactional
    public void eliminarEmpresa(UUID empresaId) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Eliminando empresa con ID: {} para tenant: {}", empresaId, tenantId);

        Empresa empresa = empresaRepository.findByEmpresaIdAndTenantId(empresaId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        empresa.setActivo(false);
        empresaRepository.save(empresa);

        log.info("Empresa desactivada exitosamente: {}", empresaId);
    }

    @Transactional
    public void toggleActivo(UUID empresaId) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Cambiando estado activo de empresa: {} para tenant: {}", empresaId, tenantId);

        Empresa empresa = empresaRepository.findByEmpresaIdAndTenantId(empresaId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        empresa.setActivo(!empresa.getActivo());
        empresaRepository.save(empresa);

        log.info("Empresa {} ahora está: {}", empresaId, empresa.getActivo() ? "ACTIVA" : "INACTIVA");
    }

    @Transactional(readOnly = true)
    public List<TipoContratistaDTO> listarTiposContratista() {
        log.info("Listando tipos de contratista");
        List<TipoContratista> tipos = tipoContratistaRepository.findAll();
        return tipoContratistaMapper.toDTOList(tipos);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO obtenerEmpresaHost() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalStateException("TenantId no establecido");
        }

        log.info("Obteniendo empresa host para tenant: {}", tenantId);

        List<Empresa> empresasHost = empresaRepository.findByTenantIdAndEsHostTrue(tenantId);
        
        if (empresasHost.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empresa host para el tenant: " + tenantId);
        }

        return empresaMapper.toDTO(empresasHost.get(0));
    }
}
