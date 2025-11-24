package com.dataservicesperu.ssoma.auth_service.config;

import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import com.dataservicesperu.ssoma.auth_service.repository.RolRepository;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            log.info("Creando usuario admin por defecto...");

            RolEntity rolSuperAdmin = rolRepository.findByCodigoRol("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol SUPER_ADMIN no existe en BD. Ejecutar init-db.sql primero."));

            UsuarioEntity admin = new UsuarioEntity();
            admin.setTenantId("SYSTEM");
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setRol(rolSuperAdmin);
            admin.setEsHost(false);
            admin.setEmpresaId(null);
            admin.setActivo(true);

            usuarioRepository.save(admin);
            log.info("Usuario admin creado exitosamente");
        } else {
            log.info("Usuario admin ya existe");
        }
    }
}
