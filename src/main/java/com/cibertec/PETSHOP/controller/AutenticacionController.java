package com.cibertec.PETSHOP.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cibertec.PETSHOP.auth.JwtUtil;
import com.cibertec.PETSHOP.dto.LoginRequest;
import com.cibertec.PETSHOP.dto.SignupRequest;
import com.cibertec.PETSHOP.dto.TokenResponse;
import com.cibertec.PETSHOP.entity.Usuario;
import com.cibertec.PETSHOP.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AutenticacionController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    // 1. LOGIN (Igual al del profesor en el proyecto modelo)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                        (request.username(), request.password()));
            
            String token = jwtUtil.generateToken(request.username());
            
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Credenciales Inválidas");
        }
    }

    // 2. REGISTRO (Lógica corregida para usar los métodos del Record SignupRequest)
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody SignupRequest signUpRequest) {
        // Validación de existencia usando accesores de record: .username() y .email()
        if (usuarioRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity.badRequest().body("Error: El Username ya esta en uso!");
        }
        
        if (usuarioRepository.existsByEmail(signUpRequest.email())) {
            return ResponseEntity.badRequest().body("Error: El Email ya esta en uso!");
        }
        
        // Creación del usuario con contraseña encriptada usando .password()
        Usuario usuario = new Usuario(
                signUpRequest.username(),
                signUpRequest.email(),
                encoder.encode(signUpRequest.password())
        );
        
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}