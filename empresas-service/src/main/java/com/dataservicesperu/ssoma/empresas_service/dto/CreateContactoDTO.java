package com.dataservicesperu.ssoma.empresas_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactoDTO {

    @Size(max = 200, message = "Nombre no debe exceder 200 caracteres")
    private String nombreContacto;

    @Size(max = 20, message = "Teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Email(message = "Email debe ser válido")
    @Size(max = 100, message = "Email no debe exceder 100 caracteres")
    private String email;

    private Boolean esPrincipal = false;
}
