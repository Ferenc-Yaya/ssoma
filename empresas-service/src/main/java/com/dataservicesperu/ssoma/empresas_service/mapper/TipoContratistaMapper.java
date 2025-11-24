package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.TipoContratistaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.TipoContratistaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TipoContratistaMapper {

    TipoContratistaDTO toDTO(TipoContratistaEntity tipo);

    List<TipoContratistaDTO> toDTOList(List<TipoContratistaEntity> tipos);
}
