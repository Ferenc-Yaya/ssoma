package com.dataservicesperu.ssoma.personas_service.mapper;

import com.dataservicesperu.ssoma.personas_service.dto.CertificacionDTO;
import com.dataservicesperu.ssoma.personas_service.entity.Certificacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CertificacionMapper {

    CertificacionDTO toDTO(Certificacion certificacion);

    List<CertificacionDTO> toDTOList(List<Certificacion> certificaciones);

    Certificacion toEntity(CertificacionDTO certificacionDTO);

    void updateEntityFromDTO(CertificacionDTO dto, @MappingTarget Certificacion entity);
}
