package com.cibertec.PETSHOP.controller;

import com.cibertec.PETSHOP.dto.SignupRequest;
import com.cibertec.PETSHOP.entity.Usuario;
import com.cibertec.PETSHOP.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth") 
public class AutenticacionController { 

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    // 2. REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody SignupRequest signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El Username ya esta en uso!");
        }
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El Email ya esta en uso!");
        }
        Usuario usuario = new Usuario(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}