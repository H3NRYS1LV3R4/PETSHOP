package com.cibertec.PETSHOP.conf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cibertec.PETSHOP.entity.Rol;
import com.cibertec.PETSHOP.entity.Usuario;
import com.cibertec.PETSHOP.repository.RolRepository;
import com.cibertec.PETSHOP.repository.UsuarioRepository;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Verificar si el usuario 'admin' ya existe para no duplicarlo
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            
            // 2. Intentamos obtener el rol 'ADMIN' que mencionas que ya existe
            // Si por alguna razón el nombre en tu BD es 'ROLE_ADMIN', cámbialo aquí
            Rol adminRol = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        // Si por error no se encuentra, lo creamos para evitar que el app falle
                        System.out.println("-> Rol ADMIN no encontrado en BD, creándolo ahora...");
                        return rolRepository.save(new Rol("ADMIN"));
                    });
            
            // 3. Crear el usuario Administrador
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            
            // Encriptamos la contraseña "admin123"
            admin.setPassword(passwordEncoder.encode("admin123")); 
            
            // Le asignamos el rol que recuperamos de la base de datos
            admin.setRoles(Set.of(adminRol));
            
            usuarioRepository.save(admin);
            
            System.out.println("--------------------------------------------------");
            System.out.println(">>> SE HA CREADO EL ADMINISTRADOR INICIAL <<<");
            System.out.println(">>> Usuario: admin");
            System.out.println(">>> Password: admin123");
            System.out.println("--------------------------------------------------");
        } else {
            System.out.println(">>> El usuario admin ya existe en la base de datos.");
        }
    }
}