package com.cibertec.PETSHOP.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        // Si la petición va dirigida a los endpoints de autenticación (login/registro), no aplicamos el filtro.
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Obtenemos el encabezado de Autorización
        String authHeader = request.getHeader("Authorization");
        
        // Verificamos que el encabezado exista y comience con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extraemos el token quitando "Bearer "
            
            // Validamos el token usando nuestra clase de utilidad
            if (!jwtUtil.isValidToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                return;
            }
            
            // Extraemos el nombre de usuario del token
            String username = jwtUtil.getUsername(token);
            
            // Si el usuario es válido y aún no está autenticado en el contexto actual
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                
                // Creamos el token de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authToken = new 
                        UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                
                // Establecemos la autenticación en el contexto global de la petición
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continuamos con el resto de los filtros
        filterChain.doFilter(request, response);
    }
}