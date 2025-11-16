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
    private final TipoContratistaRepository tipoContratistaRepository;
    private final EmpresaTipoRepository empresaTipoRepository;
    private final TipoCategoriaRepository tipoCategoriaRepository;

    private final EmpresaMapper empresaMapper;
    private final ContactoMapper contactoMapper;
    private final ServicioMapper servicioMapper;
    private final TipoContratistaMapper tipoContratistaMapper;
    private final CategoriaMapper categoriaMapper;

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

        // Asignar tipo de contratista (ÚNICO)
        if (dto.getTipoContratistaId() != null) {
            TipoContratista tipo = tipoContratistaRepository.findById(dto.getTipoContratistaId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado: " + dto.getTipoContratistaId()));

            EmpresaTipo empresaTipo = new EmpresaTipo();
            empresaTipo.setEmpresa(empresa);
            empresaTipo.setTipoContratista(tipo);
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
        List<TipoContratistaDTO> tiposDTO = obtenerTiposConCategorias(empresaId);
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
                    dto.setTipos(obtenerTiposConCategorias(empresa.getEmpresaId()));
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

        // === ACTUALIZAR TIPO DE CONTRATISTA (ÚNICO) ===
        if (dto.getTipoContratistaId() != null) {
            // Desactivar tipos existentes
            List<EmpresaTipo> tiposExistentes = empresaTipoRepository
                    .findByEmpresa_EmpresaIdAndActivoTrue(empresaId);
            for (EmpresaTipo et : tiposExistentes) {
                et.setActivo(false);
            }
            empresaTipoRepository.saveAll(tiposExistentes);

            // Buscar el tipo solicitado
            TipoContratista tipo = tipoContratistaRepository.findById(dto.getTipoContratistaId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de contratista no encontrado: " + dto.getTipoContratistaId()));

            // Verificar si ya existe una relación inactiva y reactivarla
            EmpresaTipo empresaTipoExistente = empresaTipoRepository
                    .findByEmpresa_EmpresaIdAndTipoContratista_TipoId(empresaId, dto.getTipoContratistaId())
                    .orElse(null);

            if (empresaTipoExistente != null) {
                empresaTipoExistente.setActivo(true);
                empresaTipoRepository.save(empresaTipoExistente);
            } else {
                EmpresaTipo nuevoEmpresaTipo = new EmpresaTipo();
                nuevoEmpresaTipo.setEmpresa(empresa);
                nuevoEmpresaTipo.setTipoContratista(tipo);
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
    public List<TipoContratistaDTO> listarTiposContratista() {
        log.info("Listando tipos de contratista activos");
        List<TipoContratista> tipos = tipoContratistaRepository.findByActivoTrue();
        return tipos.stream()
                .map(tipoContratistaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    private List<TipoContratistaDTO> obtenerTiposConCategorias(UUID empresaId) {
        List<EmpresaTipo> empresaTipos = empresaTipoRepository.findByEmpresa_EmpresaIdAndActivoTrue(empresaId);

        return empresaTipos.stream()
                .map(empresaTipo -> {
                    TipoContratistaDTO tipoDTO = tipoContratistaMapper.toDTO(empresaTipo.getTipoContratista());
                    List<CategoriaDTO> categoriasDTO = obtenerCategoriasAplicables(empresaTipo);
                    tipoDTO.setCategoriasAplicables(categoriasDTO);
                    return tipoDTO;
                })
                .collect(Collectors.toList());
    }

    private List<CategoriaDTO> obtenerCategoriasAplicables(EmpresaTipo empresaTipo) {
        List<TipoCategoria> tipoCategorias = tipoCategoriaRepository
                .findByTipoContratista_TipoIdAndActivoTrue(empresaTipo.getTipoContratista().getTipoId());

        return tipoCategorias.stream()
                .map(tipoCategoria -> {
                    CategoriaDTO categoriaDTO = categoriaMapper.toDTO(tipoCategoria.getCategoria());

                    empresaTipo.getCategoriasPersonalizadas().stream()
                            .filter(ec -> ec.getCategoria().getCategoriaId()
                                    .equals(tipoCategoria.getCategoria().getCategoriaId()))
                            .findFirst()
                            .ifPresentOrElse(
                                    ec -> categoriaDTO.setAplica(ec.getAplica()),
                                    () -> categoriaDTO.setAplica(true)
                            );

                    return categoriaDTO;
                })
                .collect(Collectors.toList());
    }
}
