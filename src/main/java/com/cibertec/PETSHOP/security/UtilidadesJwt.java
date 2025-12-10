package com.cibertec.PETSHOP.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class UtilidadesJwt { // <--- Nombre cambiado

    private String jwtSecret = "SecretKeyParaPetShopCibertec2025Henry";
    private int jwtExpirationMs = 86400000;

    public String generarToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String obtenerUsuarioDelToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validarToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Firma JWT invalida");
        } catch (MalformedJwtException e) {
            System.out.println("Token JWT invalido");
        } catch (ExpiredJwtException e) {
            System.out.println("Token JWT expirado");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token JWT no soportado");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadena JWT vacia");
        }
        return false;
    }
}