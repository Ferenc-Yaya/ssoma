package com.dataservicesperu.ssoma.empresas_service.service;

import com.dataservicesperu.ssoma.common.tenant.TenantContext;
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

        if (empresaRepository.existsByRuc(dto.getRuc())) {
            throw new IllegalArgumentException("Ya existe una empresa con el RUC: " + dto.getRuc());
        }

        TipoContratistaEntity tipo = tipoContratistaRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado"));

        EmpresaEntity empresa = empresaMapper.toEntity(dto);
        empresa.setTenantId(tenantId);
        empresa.setTipo(tipo);

        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContactoEntity contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setTenantId(tenantId);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        empresaRepository.save(empresa);
        log.info("Empresa creada exitosamente con ID: {}", empresa.getEmpresaId());

        return obtenerEmpresaPorId(empresa.getEmpresaId());
    }

    @Transactional(readOnly = true)
    public EmpresaDTO obtenerEmpresaPorId(UUID empresaId) {
        log.info("Obteniendo empresa con ID: {}", empresaId);

        EmpresaEntity empresa = empresaRepository.findByIdWithDetails(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        return empresaMapper.toDTO(empresa);
    }

    @Transactional(readOnly = true)
    public List<EmpresaDTO> listarEmpresasActivas() {
        log.info("Listando todas las empresas activas");

        List<EmpresaEntity> empresas = empresaRepository.findByActivoTrue();
        return empresas.stream()
                .map(empresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmpresaDTO actualizarEmpresa(UUID empresaId, CreateEmpresaDTO dto) {
        String tenantId = TenantContext.getTenantId();
        log.info("Actualizando empresa con ID: {}", empresaId);

        EmpresaEntity empresa = empresaRepository.findByIdWithDetails(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        empresaMapper.updateEntityFromDTO(dto, empresa);

        if (dto.getTipoId() != null &&
                (empresa.getTipo() == null || !empresa.getTipo().getTipoId().equals(dto.getTipoId()))) {
            TipoContratistaEntity tipo = tipoContratistaRepository.findById(dto.getTipoId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado"));
            empresa.setTipo(tipo);
        }

        // Actualizar contactos
        List<EmpresaContactoEntity> contactosExistentes = contactoRepository
                .findByEmpresa_EmpresaId(empresaId);
        contactoRepository.deleteAll(contactosExistentes);
        empresa.getContactos().clear();

        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContactoEntity contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setTenantId(tenantId);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        empresaRepository.save(empresa);
        log.info("Empresa actualizada exitosamente");

        return obtenerEmpresaPorId(empresaId);
    }

    @Transactional
    public void eliminarEmpresa(UUID empresaId) {
        log.info("Eliminando empresa con ID: {}", empresaId);

        EmpresaEntity empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        empresa.setActivo(false);
        empresaRepository.save(empresa);
        log.info("Empresa desactivada exitosamente");
    }

    @Transactional
    public void toggleActivo(UUID empresaId) {
        log.info("Cambiando estado activo de empresa: {}", empresaId);

        EmpresaEntity empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        empresa.setActivo(!empresa.getActivo());
        empresaRepository.save(empresa);
        log.info("Empresa ahora está: {}", empresa.getActivo() ? "ACTIVA" : "INACTIVA");
    }

    @Transactional(readOnly = true)
    public List<TipoContratistaDTO> listarTiposContratista() {
        log.info("Listando tipos de contratista");
        List<TipoContratistaEntity> tipos = tipoContratistaRepository.findAll();
        return tipoContratistaMapper.toDTOList(tipos);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO obtenerEmpresaHost() {
        log.info("Obteniendo empresa host");

        List<EmpresaEntity> empresasHost = empresaRepository.findByEsHostTrue();

        if (empresasHost.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empresa host");
        }

        return empresaMapper.toDTO(empresasHost.get(0));
    }
}