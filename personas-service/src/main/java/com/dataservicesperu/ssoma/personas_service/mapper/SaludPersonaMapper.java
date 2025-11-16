package com.dataservicesperu.ssoma.personas_service.mapper;

import com.dataservicesperu.ssoma.personas_service.dto.SaludPersonaDTO;
import com.dataservicesperu.ssoma.personas_service.entity.SaludPersona;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SaludPersonaMapper {

    SaludPersonaDTO toDTO(SaludPersona saludPersona);

    List<SaludPersonaDTO> toDTOList(List<SaludPersona> saludPersonas);

    SaludPersona toEntity(SaludPersonaDTO saludPersonaDTO);

    void updateEntityFromDTO(SaludPersonaDTO dto, @MappingTarget SaludPersona entity);
}
