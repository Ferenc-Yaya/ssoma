package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private UUID empresaId;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String sector;
    private Integer scoreSeguridad;
    private LocalDateTime fechaRegistro;
    private Boolean activo;

    // Relaciones
    private List<ContactoDTO> contactos;
    private List<ServicioDTO> servicios;
    private List<TipoEmpresaDTO> tipos;
}
