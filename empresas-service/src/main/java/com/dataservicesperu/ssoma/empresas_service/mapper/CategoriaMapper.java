package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.CategoriaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.Categoria;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper {

    @Mapping(target = "aplica", ignore = true) // Se establece manualmente según contexto
    CategoriaDTO toDTO(Categoria categoria);

    List<CategoriaDTO> toDTOList(List<Categoria> categorias);
}
