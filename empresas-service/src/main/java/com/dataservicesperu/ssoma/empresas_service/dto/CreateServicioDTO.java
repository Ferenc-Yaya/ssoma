package com.dataservicesperu.ssoma.empresas_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServicioDTO {

    @NotBlank(message = "Descripción del servicio es requerida")
    private String descripcionServicio;

    @Pattern(regexp = "ALTO|MEDIO|BAJO|ALTO_ESPECIALIZADO",
            message = "Nivel de riesgo debe ser: ALTO, MEDIO, BAJO o ALTO_ESPECIALIZADO")
    private String nivelRiesgo;
}
