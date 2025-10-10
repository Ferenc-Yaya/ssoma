package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.LoginRequest;
import com.dataservicesperu.ssoma.auth_service.dto.LoginResponse;
import com.dataservicesperu.ssoma.auth_service.entity.Usuario;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import com.dataservicesperu.ssoma.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Autentica usuario y genera JWT
     */
    public LoginResponse login(LoginRequest request) {
        System.out.println("========================================");
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Nombre de usuario recibido: " + request.getNombreUsuario());  // ✅ CAMBIADO
        System.out.println("Password recibido: " + request.getPassword());

        // Buscar usuario por nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario())  // ✅ CAMBIADO
                .orElseThrow(() -> {
                    System.out.println("❌ ERROR: Usuario no encontrado en la base de datos");
                    return new RuntimeException("Usuario no encontrado");
                });

        System.out.println("✅ Usuario encontrado en BD:");
        System.out.println("   - Usuario: " + usuario.getNombreUsuario());  // ✅ CAMBIADO
        System.out.println("   - Tenant: " + usuario.getTenantId());
        System.out.println("   - Role: " + usuario.getRole());
        System.out.println("   - Password en BD (hash): " + usuario.getPassword().substring(0, 30) + "...");

        // Validar contraseña con BCrypt
        System.out.println("🔐 Validando contraseña con BCrypt...");
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        System.out.println("   - Password del request: " + request.getPassword());
        System.out.println("   - Hash en BD: " + usuario.getPassword().substring(0, 20) + "...");
        System.out.println("   - Resultado: " + (passwordMatch ? "✅ MATCH" : "❌ NO MATCH"));

        if (!passwordMatch) {
            System.out.println("❌ ERROR: Contraseña incorrecta");
            throw new RuntimeException("Contraseña incorrecta");
        }

        System.out.println("✅ Autenticación exitosa, generando JWT...");

        // Generar JWT con tenant_id
        String token = jwtUtil.generateToken(
                usuario.getNombreUsuario(),  // ✅ CAMBIADO
                usuario.getTenantId(),
                usuario.getRole()
        );

        System.out.println("✅ JWT generado: " + token.substring(0, 50) + "...");
        System.out.println("========================================");

        return new LoginResponse(
                token,
                usuario.getNombreUsuario(),  // ✅ CAMBIADO
                usuario.getTenantId(),
                usuario.getRole()
        );
    }
}