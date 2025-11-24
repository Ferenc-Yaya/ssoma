package com.dataservicesperu.ssoma.empresas_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactoDTO {

    @NotBlank(message = "Nombre completo es requerido")
    @Size(max = 150, message = "Nombre no debe exceder 150 caracteres")
    private String nombreCompleto;

    @Size(max = 100, message = "Cargo no debe exceder 100 caracteres")
    private String cargo;

    @NotBlank(message = "Tipo de contacto es requerido")
    @Pattern(regexp = "REPRESENTANTE_LEGAL|RESPONSABLE_EHS|ADMIN_CONTRATO",
            message = "Tipo debe ser: REPRESENTANTE_LEGAL, RESPONSABLE_EHS o ADMIN_CONTRATO")
    private String tipoContacto;

    @Email(message = "Email debe ser válido")
    @Size(max = 150, message = "Email no debe exceder 150 caracteres")
    private String email;

    @Size(max = 20, message = "Teléfono no debe exceder 20 caracteres")
    private String telefono;

    private Boolean esPrincipal = false;
}
