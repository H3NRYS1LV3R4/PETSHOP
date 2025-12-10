package com.cibertec.PETSHOP.controller;

import com.cibertec.PETSHOP.dto.JwtResponse;
import com.cibertec.PETSHOP.dto.LoginRequest;
import com.cibertec.PETSHOP.dto.SignupRequest;
import com.cibertec.PETSHOP.entity.Rol;
import com.cibertec.PETSHOP.entity.Usuario;
import com.cibertec.PETSHOP.repository.RolRepository;
import com.cibertec.PETSHOP.repository.UsuarioRepository;
import com.cibertec.PETSHOP.security.UtilidadesJwt; // Usamos la utilidad en español

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth") 
public class AutenticacionController { 

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UtilidadesJwt utilidadesJwt; 

    // 1. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = utilidadesJwt.generarToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Usuario usuarioReal = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado"));

        return ResponseEntity.ok(new JwtResponse(jwt,
                 usuarioReal.getId(),
                 usuarioReal.getUsername(),
                 usuarioReal.getEmail(),
                 roles));
    }

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

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null) {
            Rol userRole = rolRepository.findByNombre("USUARIO")
                    .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Rol adminRole = rolRepository.findByNombre("ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));
                        roles.add(adminRole);
                        break;
                    default:
                        Rol userRole = rolRepository.findByNombre("USUARIO")
                                .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        usuario.setRoles(roles);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}