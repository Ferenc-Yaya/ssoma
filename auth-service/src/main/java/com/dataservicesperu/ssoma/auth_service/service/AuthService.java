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
        System.out.println("Email recibido: " + request.getEmail());
        System.out.println("Password recibido: " + request.getPassword());

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ ERROR: Usuario no encontrado en la base de datos");
                    return new RuntimeException("Usuario no encontrado");
                });

        System.out.println("✅ Usuario encontrado en BD:");
        System.out.println("   - Email: " + usuario.getEmail());
        System.out.println("   - Tenant: " + usuario.getTenantId());
        System.out.println("   - Role: " + usuario.getRole());
        System.out.println("   - Password en BD: " + usuario.getPassword());

        // TEMPORAL: Validar contraseña en texto plano (SIMPLIFICADO)
        System.out.println("🔐 Validando contraseña en texto plano...");
        boolean passwordMatch = usuario.getPassword().equals(request.getPassword());
        System.out.println("   Resultado: " + (passwordMatch ? "✅ MATCH" : "❌ NO MATCH"));

        if (!passwordMatch) {
            System.out.println("❌ ERROR: Contraseña incorrecta");
            throw new RuntimeException("Contraseña incorrecta");
        }

        System.out.println("✅ Autenticación exitosa, generando JWT...");

        // Generar JWT con tenant_id
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getTenantId(),
                usuario.getRole()
        );

        System.out.println("✅ JWT generado: " + token.substring(0, 50) + "...");
        System.out.println("========================================");

        return new LoginResponse(
                token,
                usuario.getEmail(),
                usuario.getTenantId(),
                usuario.getRole()
        );
    }
}