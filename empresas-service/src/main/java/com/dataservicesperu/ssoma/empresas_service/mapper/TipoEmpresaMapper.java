package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.TipoEmpresaDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.TipoEmpresa;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {RequisitoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TipoEmpresaMapper {

    @Mapping(target = "requisitosAplicables", ignore = true) // Se mapea manualmente
    TipoEmpresaDTO toDTO(TipoEmpresa tipo);

    List<TipoEmpresaDTO> toDTOList(List<TipoEmpresa> tipos);
}
