package com.dataservicesperu.ssoma.auth_service.mapper;

import com.dataservicesperu.ssoma.auth_service.dto.RolDTO;
import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolMapper {

    RolEntity toEntity(RolDTO dto);

    RolDTO toDto(RolEntity entity);

    List<RolDTO> toDtoList(List<RolEntity> entities);

    void updateEntity(RolDTO dto, @MappingTarget RolEntity entity);
}
