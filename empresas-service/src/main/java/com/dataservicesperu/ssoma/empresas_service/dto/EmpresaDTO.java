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
    private String tenantId;
    private String ruc;
    private String razonSocial;
    private TipoContratistaDTO tipo;
    private String direccion;
    private String sitioWeb;
    private String rubroComercial;
    private Integer scoreSeguridad;
    private String estadoHabilitacion;
    private Boolean activo;
    private Boolean esHost;
    private LocalDateTime createdAt;
    private List<ContactoDTO> contactos;
}
