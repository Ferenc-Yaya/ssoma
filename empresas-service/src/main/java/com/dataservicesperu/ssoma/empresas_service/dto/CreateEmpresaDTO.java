package com.dataservicesperu.ssoma.empresas_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmpresaDTO {

    @NotBlank(message = "RUC es requerido")
    @Size(min = 8, max = 20, message = "RUC debe tener entre 8 y 20 caracteres")
    private String ruc;

    @NotBlank(message = "Razón social es requerida")
    @Size(max = 200, message = "Razón social no debe exceder 200 caracteres")
    private String razonSocial;

    @Size(max = 255, message = "Dirección no debe exceder 255 caracteres")
    private String direccion;

    @Size(max = 100, message = "Sector no debe exceder 100 caracteres")
    private String sector;

    @Min(value = 0, message = "Score debe ser mayor o igual a 0")
    @Max(value = 100, message = "Score debe ser menor o igual a 100")
    private Integer scoreSeguridad;

    // Relaciones
    @Valid
    private List<CreateContactoDTO> contactos;

    @Valid
    private List<CreateServicioDTO> servicios;

    @NotNull(message = "Debe especificar el tipo de empresa")
    private UUID tipoEmpresaId;  // ID del tipo de empresa
}
