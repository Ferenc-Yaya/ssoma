package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.TipoContratistaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.TipoContratista;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TipoContratistaMapper {

    TipoContratistaDTO toDTO(TipoContratista tipo);

    List<TipoContratistaDTO> toDTOList(List<TipoContratista> tipos);
}
