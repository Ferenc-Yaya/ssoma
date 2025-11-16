package com.dataservicesperu.ssoma.personas_service.mapper;

import com.dataservicesperu.ssoma.personas_service.dto.CreatePersonaDTO;
import com.dataservicesperu.ssoma.personas_service.dto.PersonaDTO;
import com.dataservicesperu.ssoma.personas_service.entity.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PersonaMapper {

    PersonaDTO toDTO(Persona persona);

    List<PersonaDTO> toDTOList(List<Persona> personas);

    Persona toEntity(PersonaDTO personaDTO);

    Persona toEntity(CreatePersonaDTO createPersonaDTO);

    void updateEntityFromDTO(PersonaDTO dto, @MappingTarget Persona entity);
}
