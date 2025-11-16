package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.TipoContratistaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.TipoContratista;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CategoriaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TipoContratistaMapper {

    @Mapping(target = "categoriasAplicables", ignore = true) // Se mapea manualmente
    TipoContratistaDTO toDTO(TipoContratista tipo);

    List<TipoContratistaDTO> toDTOList(List<TipoContratista> tipos);
}
