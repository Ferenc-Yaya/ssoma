package com.dataservicesperu.ssoma.personas_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_personas")
@Data
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "persona_id")
    private UUID personaId;

    @Column(name = "empresa_id")
    private UUID empresaId;

    @Column(name = "tipo_persona", nullable = false, length = 50)
    private String tipoPersona;

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @Column(name = "dni", nullable = false, length = 20)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;
}
