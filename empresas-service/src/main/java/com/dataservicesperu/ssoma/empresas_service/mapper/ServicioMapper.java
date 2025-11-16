package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateServicioDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.ServicioDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaServicio;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServicioMapper {

    ServicioDTO toDTO(EmpresaServicio servicio);

    List<ServicioDTO> toDTOList(List<EmpresaServicio> servicios);

    @Mapping(target = "servicioId", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "activo", constant = "true")
    EmpresaServicio toEntity(CreateServicioDTO dto);

    @Mapping(target = "servicioId", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDTO(CreateServicioDTO dto, @MappingTarget EmpresaServicio servicio);
}
