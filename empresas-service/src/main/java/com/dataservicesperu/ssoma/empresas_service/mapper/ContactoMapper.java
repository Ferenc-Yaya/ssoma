package com.dataservicesperu.ssoma.empresas_service.mapper;

import com.dataservicesperu.ssoma.empresas_service.dto.ContactoDTO;
import com.dataservicesperu.ssoma.empresas_service.dto.CreateContactoDTO;
import com.dataservicesperu.ssoma.empresas_service.entity.EmpresaContactoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactoMapper {

    ContactoDTO toDTO(EmpresaContactoEntity contacto);

    List<ContactoDTO> toDTOList(List<EmpresaContactoEntity> contactos);

    @Mapping(target = "contactoId", ignore = true)
    @Mapping(target = "tenantId", ignore = true) // Se establece en el servicio
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EmpresaContactoEntity toEntity(CreateContactoDTO dto);

    @Mapping(target = "contactoId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(CreateContactoDTO dto, @MappingTarget EmpresaContactoEntity contacto);
}
