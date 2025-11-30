package com.proyectoweb.auth.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response del login exitoso con token JWT")
public record LoginResponseDto(
        @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        
        @Schema(description = "ID del usuario")
        UUID userId,
        
        @Schema(description = "Email del usuario", example = "juan.perez@example.com")
        String email,
        
        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        String fullName,
        
        @Schema(description = "Rol del usuario", example = "CLIENTE")
        String role,
        
        @Schema(description = "ID de la empresa asignada (null para clientes)", nullable = true)
        UUID tenantId
) {}
