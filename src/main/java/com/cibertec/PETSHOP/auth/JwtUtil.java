package com.cibertec.PETSHOP.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.expirationMs:3600000}")
	private long expirationMs;

	// Clave secreta para la firma del token
	private final String SECRET_KEY = "ClaveSecretaMuyLargaYCompletamenteAleatoriaParaPetshop2024!#*";

	private Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

	// Generación del token
	public String generateToken(String username) {
		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationMs)).claim("proyecto", "PETSHOP")
				.signWith(key).compact();
	}

	// Obtener el usuario del token
	public String getUsername(String token) {
		return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload().getSubject();
	}

	// Validación del token
	public boolean isValidToken(String token) {
		try {
			Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}