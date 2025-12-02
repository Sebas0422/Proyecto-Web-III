package com.proyectoweb.reports.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;
    
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        logger.info("=================================================");
        logger.info("JWT SECRET CONFIGURADO: {}", secret);
        logger.info("JWT SECRET LENGTH: {}", secret != null ? secret.length() : "NULL");
        logger.info("=================================================");
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractTenantId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("tenantId", String.class);
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            logger.info("🔍 Validando token JWT...");
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            logger.info("✅ Token validado exitosamente");
            return true;
        } catch (Exception e) {
            logger.error("❌ Error validando token: {} - Tipo: {}", e.getMessage(), e.getClass().getSimpleName());
            logger.error("Secret length: {}", secret.length());
            e.printStackTrace();
            return false;
        }
    }
}
