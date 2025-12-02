package com.proyectoweb.reports.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        logger.info("📨 Request a: {} - Header Authorization: {}", request.getRequestURI(), 
                   authHeader != null ? "Bearer [presente]" : "NO PRESENTE");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("🔑 Token extraído, validando...");

            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);
                String tenantId = jwtUtil.extractTenantId(token);
                
                logger.info("✅ Usuario autenticado: userId={}, role={}, tenantId={}", userId, role, tenantId);

                // Store tenantId in request attribute for controllers
                request.setAttribute("tenantId", tenantId);
                request.setAttribute("userId", userId);
                request.setAttribute("role", role);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("⛔ Token inválido, request no autenticado");
            }
        } else {
            logger.warn("⚠️ No se encontró token Bearer en el header");
        }

        filterChain.doFilter(request, response);
    }
}
