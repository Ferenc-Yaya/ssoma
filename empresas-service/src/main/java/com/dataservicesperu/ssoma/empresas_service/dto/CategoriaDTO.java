package com.dataservicesperu.ssoma.empresas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    private UUID categoriaId;
    private String codigo;
    private String nombre;
    private Boolean aplica; // Si fue personalizado
}
