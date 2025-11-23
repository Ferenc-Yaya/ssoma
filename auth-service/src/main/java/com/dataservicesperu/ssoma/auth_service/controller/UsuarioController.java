package com.dataservicesperu.ssoma.auth_service.controller;

import com.dataservicesperu.ssoma.auth_service.entity.Usuario;
import com.dataservicesperu.ssoma.auth_service.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        try {
            if (usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                return ResponseEntity.status(400).body(null);
            }

            usuario.setId(UUID.randomUUID());
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            Usuario guardado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return usuarioRepository.findById(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable String id, @RequestBody Usuario usuarioActualizado) {
        try {
            UUID uuid = UUID.fromString(id);
            return usuarioRepository.findById(uuid)
                    .map(usuario -> {
                        usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());

                        // Solo actualizar password si viene en el request
                        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
                        }

                        usuario.setTenantId(usuarioActualizado.getTenantId());
                        usuario.setRole(usuarioActualizado.getRole());

                        Usuario guardado = usuarioRepository.save(usuario);
                        return ResponseEntity.ok(guardado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // DELETE - Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (usuarioRepository.existsById(uuid)) {
                usuarioRepository.deleteById(uuid);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
