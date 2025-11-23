package com.dataservicesperu.ssoma.activos_service.mapper;

import com.dataservicesperu.ssoma.activos_service.dto.*;
import com.dataservicesperu.ssoma.activos_service.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ActivoMapper {

    // ========================================
    // TipoActivo
    // ========================================

    TipoActivoDTO toDTO(TipoActivo entity);

    List<TipoActivoDTO> toDTOList(List<TipoActivo> entities);

    @Mapping(target = "tipoId", ignore = true)
    TipoActivo toEntity(TipoActivoDTO dto);

    // ========================================
    // CategoriaActivo
    // ========================================

    @Mapping(target = "tipoNombre", source = "tipo.nombre")
    CategoriaActivoDTO toDTO(CategoriaActivo entity);

    List<CategoriaActivoDTO> categoriaToDTOList(List<CategoriaActivo> entities);

    @Mapping(target = "categoriaId", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    CategoriaActivo toEntity(CategoriaActivoDTO dto);

    // ========================================
    // Activo (Principal)
    // ========================================

    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    @Mapping(target = "tipoNombre", source = "categoria.tipo.nombre")
    @Mapping(target = "requisitos", source = "requisitos")
    @Mapping(target = "operadores", source = "operadores")
    ActivoDTO toDTO(Activo entity);

    List<ActivoDTO> activosToDTOList(List<Activo> entities);

    // ========================================
    // ActivoRequisito
    // ========================================

    @Mapping(target = "requisitoNombre", source = "requisito.nombre")
    @Mapping(target = "requisitoCodigo", source = "requisito.codigo")
    @Mapping(target = "requisitoObligatorio", source = "requisito.obligatorio")
    ActivoRequisitoDTO toDTO(ActivoRequisito entity);

    List<ActivoRequisitoDTO> requisitosToDTOList(List<ActivoRequisito> entities);

    // ========================================
    // Operador
    // ========================================

    OperadorSimpleDTO toSimpleDTO(Operador entity);

    List<OperadorSimpleDTO> operadoresToSimpleDTOList(List<Operador> entities);
}
