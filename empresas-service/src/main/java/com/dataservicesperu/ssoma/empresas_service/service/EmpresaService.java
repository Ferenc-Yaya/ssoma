package com.dataservicesperu.ssoma.empresas_service.service;

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
    private final EmpresaServicioRepository servicioRepository;
    private final TipoEmpresaRepository tipoEmpresaRepository;
    private final EmpresaTipoRepository empresaTipoRepository;
    private final TipoRequisitoRepository tipoRequisitoRepository;

    private final EmpresaMapper empresaMapper;
    private final ContactoMapper contactoMapper;
    private final ServicioMapper servicioMapper;
    private final TipoEmpresaMapper tipoEmpresaMapper;
    private final RequisitoMapper requisitoMapper;

    @Transactional
    public EmpresaDTO crearEmpresa(CreateEmpresaDTO dto) {
        log.info("Creando empresa con RUC: {}", dto.getRuc());

        if (empresaRepository.existsByRuc(dto.getRuc())) {
            throw new IllegalArgumentException("Ya existe una empresa con el RUC: " + dto.getRuc());
        }

        // Crear la entidad empresa
        Empresa empresa = empresaMapper.toEntity(dto);

        // Agregar contactos
        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContacto contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        // Agregar servicios
        if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
            for (CreateServicioDTO servicioDTO : dto.getServicios()) {
                EmpresaServicio servicio = servicioMapper.toEntity(servicioDTO);
                servicio.setEmpresa(empresa);
                empresa.getServicios().add(servicio);
            }
        }

        // Guardar empresa
        empresaRepository.save(empresa);

        // Asignar tipo de empresa (ÚNICO)
        if (dto.getTipoEmpresaId() != null) {
            TipoEmpresa tipo = tipoEmpresaRepository.findById(dto.getTipoEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de empresa no encontrado: " + dto.getTipoEmpresaId()));

            EmpresaTipo empresaTipo = new EmpresaTipo();
            empresaTipo.setEmpresa(empresa);
            empresaTipo.setTipoEmpresa(tipo);
            empresaTipo.setActivo(true);

            empresaTipoRepository.save(empresaTipo);
        }

        log.info("Empresa creada exitosamente con ID: {}", empresa.getEmpresaId());
        return obtenerEmpresaPorId(empresa.getEmpresaId());
    }

    @Transactional(readOnly = true)
    public EmpresaDTO obtenerEmpresaPorId(UUID empresaId) {
        log.info("Obteniendo empresa con ID: {}", empresaId);

        Empresa empresa = empresaRepository.findByIdWithDetails(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        EmpresaDTO dto = empresaMapper.toDTO(empresa);
        List<TipoEmpresaDTO> tiposDTO = obtenerTiposConRequisitos(empresaId);
        dto.setTipos(tiposDTO);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<EmpresaDTO> listarEmpresasActivas() {
        log.info("Listando todas las empresas activas");

        List<Empresa> empresas = empresaRepository.findByActivoTrue();
        return empresas.stream()
                .map(empresa -> {
                    EmpresaDTO dto = empresaMapper.toDTO(empresa);
                    dto.setTipos(obtenerTiposConRequisitos(empresa.getEmpresaId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public EmpresaDTO actualizarEmpresa(UUID empresaId, CreateEmpresaDTO dto) {
        log.info("Actualizando empresa con ID: {}", empresaId);

        Empresa empresa = empresaRepository.findByIdWithDetails(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        // Actualizar datos básicos
        empresaMapper.updateEntityFromDTO(dto, empresa);

        // === ACTUALIZAR CONTACTOS ===
        List<EmpresaContacto> contactosExistentes = contactoRepository
                .findByEmpresa_EmpresaIdAndActivoTrue(empresaId);
        contactoRepository.deleteAll(contactosExistentes);
        empresa.getContactos().clear();

        if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
            for (CreateContactoDTO contactoDTO : dto.getContactos()) {
                EmpresaContacto contacto = contactoMapper.toEntity(contactoDTO);
                contacto.setEmpresa(empresa);
                empresa.getContactos().add(contacto);
            }
        }

        // === ACTUALIZAR SERVICIOS ===
        List<EmpresaServicio> serviciosExistentes = servicioRepository
                .findByEmpresa_EmpresaIdAndActivoTrue(empresaId);
        servicioRepository.deleteAll(serviciosExistentes);
        empresa.getServicios().clear();

        if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
            for (CreateServicioDTO servicioDTO : dto.getServicios()) {
                EmpresaServicio servicio = servicioMapper.toEntity(servicioDTO);
                servicio.setEmpresa(empresa);
                empresa.getServicios().add(servicio);
            }
        }

        // === ACTUALIZAR TIPO DE EMPRESA (ÚNICO) ===
        if (dto.getTipoEmpresaId() != null) {
            // Desactivar tipos existentes
            List<EmpresaTipo> tiposExistentes = empresaTipoRepository
                    .findByEmpresa_EmpresaIdAndActivoTrue(empresaId);
            for (EmpresaTipo et : tiposExistentes) {
                et.setActivo(false);
            }
            empresaTipoRepository.saveAll(tiposExistentes);

            // Buscar el tipo solicitado
            TipoEmpresa tipo = tipoEmpresaRepository.findById(dto.getTipoEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de empresa no encontrado: " + dto.getTipoEmpresaId()));

            // Verificar si ya existe una relación inactiva y reactivarla
            EmpresaTipo empresaTipoExistente = empresaTipoRepository
                    .findByEmpresa_EmpresaIdAndTipoEmpresa_TipoId(empresaId, dto.getTipoEmpresaId())
                    .orElse(null);

            if (empresaTipoExistente != null) {
                empresaTipoExistente.setActivo(true);
                empresaTipoRepository.save(empresaTipoExistente);
            } else {
                EmpresaTipo nuevoEmpresaTipo = new EmpresaTipo();
                nuevoEmpresaTipo.setEmpresa(empresa);
                nuevoEmpresaTipo.setTipoEmpresa(tipo);
                nuevoEmpresaTipo.setActivo(true);
                empresaTipoRepository.save(nuevoEmpresaTipo);
            }
        }

        empresaRepository.save(empresa);

        log.info("Empresa actualizada exitosamente: {}", empresaId);
        return obtenerEmpresaPorId(empresaId);
    }

    @Transactional
    public void eliminarEmpresa(UUID empresaId) {
        log.info("Eliminando empresa con ID: {}", empresaId);

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        empresa.setActivo(false);
        empresaRepository.save(empresa);

        log.info("Empresa desactivada exitosamente: {}", empresaId);
    }

    @Transactional
    public void toggleActivo(UUID empresaId) {
        log.info("Cambiando estado activo de empresa: {}", empresaId);

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada: " + empresaId));

        empresa.setActivo(!empresa.getActivo());
        empresaRepository.save(empresa);

        log.info("Empresa {} ahora está: {}", empresaId, empresa.getActivo() ? "ACTIVA" : "INACTIVA");
    }

    @Transactional(readOnly = true)
    public List<TipoEmpresaDTO> listarTiposEmpresa() {
        log.info("Listando tipos de empresa activos");
        List<TipoEmpresa> tipos = tipoEmpresaRepository.findByActivoTrue();
        return tipos.stream()
                .map(tipoEmpresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    private List<TipoEmpresaDTO> obtenerTiposConRequisitos(UUID empresaId) {
        List<EmpresaTipo> empresaTipos = empresaTipoRepository.findByEmpresa_EmpresaIdAndActivoTrue(empresaId);

        return empresaTipos.stream()
                .map(empresaTipo -> {
                    TipoEmpresaDTO tipoDTO = tipoEmpresaMapper.toDTO(empresaTipo.getTipoEmpresa());
                    List<RequisitoDTO> requisitosDTO = obtenerRequisitosAplicables(empresaTipo);
                    tipoDTO.setRequisitosAplicables(requisitosDTO);
                    return tipoDTO;
                })
                .collect(Collectors.toList());
    }

    private List<RequisitoDTO> obtenerRequisitosAplicables(EmpresaTipo empresaTipo) {
        List<TipoRequisito> tipoRequisitos = tipoRequisitoRepository
                .findByTipoEmpresa_TipoIdAndActivoTrue(empresaTipo.getTipoEmpresa().getTipoId());

        return tipoRequisitos.stream()
                .map(tipoRequisito -> {
                    RequisitoDTO requisitoDTO = requisitoMapper.toDTO(tipoRequisito.getRequisito());

                    empresaTipo.getRequisitosPersonalizados().stream()
                            .filter(er -> er.getRequisito().getCategoriaId()
                                    .equals(tipoRequisito.getRequisito().getCategoriaId()))
                            .findFirst()
                            .ifPresentOrElse(
                                    er -> requisitoDTO.setAplica(er.getAplica()),
                                    () -> requisitoDTO.setAplica(true)
                            );

                    return requisitoDTO;
                })
                .collect(Collectors.toList());
    }
}
