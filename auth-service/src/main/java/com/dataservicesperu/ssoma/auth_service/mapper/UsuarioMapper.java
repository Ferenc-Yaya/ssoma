package com.dataservicesperu.ssoma.auth_service.mapper;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "passwordHash", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    @Mapping(target = "password", ignore = true)
    UsuarioDTO toDto(UsuarioEntity entity);

    List<UsuarioDTO> toDtoList(List<UsuarioEntity> entities);

    @Mapping(target = "passwordHash", ignore = true)
    void updateEntity(UsuarioDTO dto, @MappingTarget UsuarioEntity entity);
}
