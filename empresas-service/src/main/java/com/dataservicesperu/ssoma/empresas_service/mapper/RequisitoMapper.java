package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.RequisitoDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.Requisito;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequisitoMapper {

    @Mapping(target = "aplica", ignore = true) // Se establece manualmente según contexto
    RequisitoDTO toDTO(Requisito requisito);

    List<RequisitoDTO> toDTOList(List<Requisito> requisitos);
}
