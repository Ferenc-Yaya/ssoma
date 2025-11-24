package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.CreateEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.EmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.Empresa;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ContactoMapper.class, TipoContratistaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmpresaMapper {

    @Mapping(target = "contactos", source = "contactos")
    @Mapping(target = "tipo", source = "tipo")
    EmpresaDTO toDTO(Empresa empresa);

    List<EmpresaDTO> toDTOList(List<Empresa> empresas);

    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "tenantId", ignore = true) // Se establece en el servicio
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "contactos", ignore = true) // Se mapea manualmente
    @Mapping(target = "tipo", ignore = true) // Se mapea manualmente
    Empresa toEntity(CreateEmpresaDTO dto);

    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "contactos", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    void updateEntityFromDTO(CreateEmpresaDTO dto, @MappingTarget Empresa empresa);
}
