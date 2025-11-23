package com.dataservicesperu.ssoma.roles_service.mapper;

import com.dataservicesperu.ssoma.roles_service.dto.RoleDTO;
import com.dataservicesperu.ssoma.roles_service.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleEntity toEntity(RoleDTO dto);
    RoleDTO toDto(RoleEntity entity);
    List<RoleDTO> toDtoList(List<RoleEntity> entities);

    void updateEntity(RoleDTO dto, @MappingTarget RoleEntity entity);
}