package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.Empresa;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ContactoMapper.class, ServicioMapper.class, TipoContratistaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmpresaMapper {

    @Mapping(target = "contactos", source = "contactos")
    @Mapping(target = "servicios", source = "servicios")
    @Mapping(target = "tipos", ignore = true) // Se mapea manualmente en el service
    EmpresaDTO toDTO(Empresa empresa);

    List<EmpresaDTO> toDTOList(List<Empresa> empresas);

    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "contactos", ignore = true) // Se mapea manualmente
    @Mapping(target = "servicios", ignore = true) // Se mapea manualmente
    @Mapping(target = "tipos", ignore = true) // Se mapea manualmente
    Empresa toEntity(CreateEmpresaDTO dto);

    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "contactos", ignore = true)
    @Mapping(target = "servicios", ignore = true)
    @Mapping(target = "tipos", ignore = true)
    void updateEntityFromDTO(CreateEmpresaDTO dto, @MappingTarget Empresa empresa);
}
