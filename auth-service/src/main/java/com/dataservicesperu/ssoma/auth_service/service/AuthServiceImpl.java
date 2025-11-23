package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.LoginRequest;
import com.dataservicesperu.ssoma.auth_service.dto.LoginResponse;
import com.dataservicesperu.ssoma.auth_service.entity.UsuarioEntity;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import com.dataservicesperu.ssoma.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(
                usuario.getUsername(),
                usuario.getTenantId(),
                usuario.getEmpresaId(),
                usuario.getEsHost(),
                usuario.getCodigoRol()
        );

        return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .tenantId(usuario.getTenantId())
                .empresaId(usuario.getEmpresaId())
                .esHost(usuario.getEsHost())
                .codigoRol(usuario.getCodigoRol())
                .build();
    }
}
