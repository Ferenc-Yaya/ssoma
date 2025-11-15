package com.dataservicesperu.ssoma.auth_service.config;

import com.dataservicesperu.ssoma.auth_service.entity.Usuario;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class DataInitializationConfig {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return encoder;
    }

    @Bean
    public CommandLineRunner initializeAdminUser(UsuarioRepository usuarioRepository) {
        return args -> {
            try {
                // Buscar usuario admin existente
                Optional<Usuario> existingAdmin = usuarioRepository.findByNombreUsuario("admin");

                if (existingAdmin.isPresent()) {
                    // Si existe, convertirlo en superadmin
                    Usuario admin = existingAdmin.get();
                    admin.setIsSuperAdmin(true);
                    admin.setRole("SUPER_ADMIN");

                    // Opcional: Permitir acceso a todos los tenants manteniendo tenant_a como principal
                    // admin.setTenantId("public");

                    usuarioRepository.save(admin);

                    System.out.println("========================================");
                    System.out.println("¡Usuario admin ahora es SUPER_ADMIN!");
                    System.out.println("Usuario: admin / Password: admin");
                    System.out.println("========================================");
                } else {
                    // Si no existe, créalo como superadmin
                    Usuario admin = new Usuario();
                    admin.setId(UUID.randomUUID());
                    admin.setNombreUsuario("admin");
                    admin.setPassword(passwordEncoder().encode("admin"));
                    admin.setTenantId("tenant_a");
                    admin.setRole("SUPER_ADMIN");
                    admin.setIsSuperAdmin(true);

                    usuarioRepository.save(admin);

                    System.out.println("========================================");
                    System.out.println("¡Admin creado como SUPER_ADMIN!");
                    System.out.println("Usuario: admin / Password: admin");
                    System.out.println("========================================");
                }
            } catch (Exception e) {
                System.err.println("ERROR al configurar admin como superadmin: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    // Método de utilidad para crear usuarios
    private void createUserIfNotExists(
            UsuarioRepository repo,
            String username,
            String password,
            String tenantId,
            String role,
            boolean isSuperAdmin) {

        if (repo.findByNombreUsuario(username).isEmpty()) {
            Usuario user = new Usuario();
            user.setId(UUID.randomUUID());
            user.setNombreUsuario(username);
            user.setPassword(encoder.encode(password));
            user.setTenantId(tenantId);
            user.setRole(role);
            user.setIsSuperAdmin(isSuperAdmin);

            repo.save(user);

            System.out.println("Usuario creado: " + username + " (Tenant: " + tenantId + ", Rol: " + role + ")");
        }
    }
}
