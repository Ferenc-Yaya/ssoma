package com.dataservicesperu.ssoma.auth_service.mapper;

import com.dataservicesperu.ssoma.auth_service.dto.UsuarioDTO;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(source = "rol.rolId", target = "rolId")
    @Mapping(source = "rol.codigoRol", target = "codigoRol")
    @Mapping(source = "rol.nombreMostrar", target = "nombreRol")
    @Mapping(target = "password", ignore = true)
    UsuarioDTO toDto(UsuarioEntity entity);

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);
}
