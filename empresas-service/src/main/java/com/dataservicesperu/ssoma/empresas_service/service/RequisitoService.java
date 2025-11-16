package com.dataservicesperu.ssoma.empresas_service.service;

import com.dataservicesperu.ssoma.empresas_service.dto.RequisitoDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.*;
import com.dataservicesperu.ssoma.empresas_service.mapper.RequisitoMapper;
import com.dataservicesperu.ssoma.empresas_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar requisitos personalizados por empresa
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequisitoService {

    private final EmpresaTipoRepository empresaTipoRepository;
    private final EmpresaRequisitoRepository empresaRequisitoRepository;
    private final RequisitoRepository requisitoRepository;
    private final RequisitoMapper requisitoMapper;

    /**
     * Obtener requisitos aplicables para una empresa y tipo específico
     */
    @Transactional(readOnly = true)
    public List<RequisitoDTO> obtenerRequisitosAplicables(UUID empresaId, UUID tipoId) {
        log.info("Obteniendo requisitos aplicables para empresa {} y tipo {}", empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoEmpresa_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró relación entre empresa y tipo de empresa"));

        // Obtener requisitos del tipo (default)
        List<TipoRequisito> tipoRequisitos = empresaTipo.getTipoEmpresa().getRequisitos()
                .stream()
                .filter(TipoRequisito::getActivo)
                .collect(Collectors.toList());

        return tipoRequisitos.stream()
                .map(tipoRequisito -> {
                    RequisitoDTO dto = requisitoMapper.toDTO(tipoRequisito.getRequisito());

                    // Verificar si hay override personalizado
                    empresaTipo.getRequisitosPersonalizados().stream()
                            .filter(er -> er.getRequisito().getCategoriaId()
                                    .equals(tipoRequisito.getRequisito().getCategoriaId()))
                            .findFirst()
                            .ifPresentOrElse(
                                    er -> dto.setAplica(er.getAplica()),
                                    () -> dto.setAplica(true)
                            );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Crear excepción: desactivar un requisito para una empresa específica
     */
    @Transactional
    public void desactivarRequisitoParaEmpresa(UUID empresaId, UUID tipoId, UUID requisitoId) {
        log.info("Desactivando requisito {} para empresa {} tipo {}", requisitoId, empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoEmpresa_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException("Relación no encontrada"));

        Requisito requisito = requisitoRepository.findById(requisitoId)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado"));

        // Buscar si ya existe una configuración personalizada
        EmpresaRequisito empresaRequisito = empresaRequisitoRepository
                .findByEmpresaTipo_EmpresaTipoIdAndRequisito_CategoriaId(
                        empresaTipo.getEmpresaTipoId(), requisitoId)
                .orElseGet(() -> {
                    EmpresaRequisito nueva = new EmpresaRequisito();
                    nueva.setEmpresaTipo(empresaTipo);
                    nueva.setRequisito(requisito);
                    nueva.setActivo(true);
                    return nueva;
                });

        empresaRequisito.setAplica(false);
        empresaRequisitoRepository.save(empresaRequisito);

        log.info("Requisito desactivado exitosamente");
    }

    /**
     * Crear excepción: activar un requisito para una empresa específica
     */
    @Transactional
    public void activarRequisitoParaEmpresa(UUID empresaId, UUID tipoId, UUID requisitoId) {
        log.info("Activando requisito {} para empresa {} tipo {}", requisitoId, empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoEmpresa_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException("Relación no encontrada"));

        Requisito requisito = requisitoRepository.findById(requisitoId)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado"));

        EmpresaRequisito empresaRequisito = empresaRequisitoRepository
                .findByEmpresaTipo_EmpresaTipoIdAndRequisito_CategoriaId(
                        empresaTipo.getEmpresaTipoId(), requisitoId)
                .orElseGet(() -> {
                    EmpresaRequisito nueva = new EmpresaRequisito();
                    nueva.setEmpresaTipo(empresaTipo);
                    nueva.setRequisito(requisito);
                    nueva.setActivo(true);
                    return nueva;
                });

        empresaRequisito.setAplica(true);
        empresaRequisitoRepository.save(empresaRequisito);

        log.info("Requisito activado exitosamente");
    }
}
