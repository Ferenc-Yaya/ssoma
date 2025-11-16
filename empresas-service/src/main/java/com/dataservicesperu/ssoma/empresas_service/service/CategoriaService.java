package com.dataservicesperu.ssoma.empresas_service.service;

import com.dataservicesperu.ssoma.empresas_service.dto.CategoriaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.*;
import com.dataservicesperu.ssoma.empresas_service.mapper.CategoriaMapper;
import com.dataservicesperu.ssoma.empresas_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar categorías personalizadas por empresa
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {

    private final EmpresaTipoRepository empresaTipoRepository;
    private final EmpresaCategoriaRepository empresaCategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    /**
     * Obtener categorías aplicables para una empresa y tipo específico
     */
    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerCategoriasAplicables(UUID empresaId, UUID tipoId) {
        log.info("Obteniendo categorías aplicables para empresa {} y tipo {}", empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoContratista_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró relación entre empresa y tipo de contratista"));

        // Obtener categorías del tipo (default)
        List<TipoCategoria> tipoCategorias = empresaTipo.getTipoContratista().getCategorias()
                .stream()
                .filter(TipoCategoria::getActivo)
                .collect(Collectors.toList());

        return tipoCategorias.stream()
                .map(tipoCategoria -> {
                    CategoriaDTO dto = categoriaMapper.toDTO(tipoCategoria.getCategoria());

                    // Verificar si hay override personalizado
                    empresaTipo.getCategoriasPersonalizadas().stream()
                            .filter(ec -> ec.getCategoria().getCategoriaId()
                                    .equals(tipoCategoria.getCategoria().getCategoriaId()))
                            .findFirst()
                            .ifPresentOrElse(
                                    ec -> dto.setAplica(ec.getAplica()),
                                    () -> dto.setAplica(true)
                            );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Crear excepción: desactivar una categoría para una empresa específica
     */
    @Transactional
    public void desactivarCategoriaParaEmpresa(UUID empresaId, UUID tipoId, UUID categoriaId) {
        log.info("Desactivando categoría {} para empresa {} tipo {}", categoriaId, empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoContratista_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException("Relación no encontrada"));

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        // Buscar si ya existe una configuración personalizada
        EmpresaCategoria empresaCategoria = empresaCategoriaRepository
                .findByEmpresaTipo_EmpresaTipoIdAndCategoria_CategoriaId(
                        empresaTipo.getEmpresaTipoId(), categoriaId)
                .orElseGet(() -> {
                    EmpresaCategoria nueva = new EmpresaCategoria();
                    nueva.setEmpresaTipo(empresaTipo);
                    nueva.setCategoria(categoria);
                    nueva.setActivo(true);
                    return nueva;
                });

        empresaCategoria.setAplica(false);
        empresaCategoriaRepository.save(empresaCategoria);

        log.info("Categoría desactivada exitosamente");
    }

    /**
     * Crear excepción: activar una categoría para una empresa específica
     */
    @Transactional
    public void activarCategoriaParaEmpresa(UUID empresaId, UUID tipoId, UUID categoriaId) {
        log.info("Activando categoría {} para empresa {} tipo {}", categoriaId, empresaId, tipoId);

        EmpresaTipo empresaTipo = empresaTipoRepository
                .findByEmpresa_EmpresaIdAndTipoContratista_TipoId(empresaId, tipoId)
                .orElseThrow(() -> new IllegalArgumentException("Relación no encontrada"));

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        EmpresaCategoria empresaCategoria = empresaCategoriaRepository
                .findByEmpresaTipo_EmpresaTipoIdAndCategoria_CategoriaId(
                        empresaTipo.getEmpresaTipoId(), categoriaId)
                .orElseGet(() -> {
                    EmpresaCategoria nueva = new EmpresaCategoria();
                    nueva.setEmpresaTipo(empresaTipo);
                    nueva.setCategoria(categoria);
                    nueva.setActivo(true);
                    return nueva;
                });

        empresaCategoria.setAplica(true);
        empresaCategoriaRepository.save(empresaCategoria);

        log.info("Categoría activada exitosamente");
    }
}
