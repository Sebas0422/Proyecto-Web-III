package com.proyectoweb.auth.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request para registrar un nuevo usuario")
public record RegisterUserRequest(
        @Schema(description = "Email del usuario", example = "juan.perez@example.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        String email,

        @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "password123")
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @Schema(description = "Nombre del usuario", example = "Juan")
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @Schema(description = "Apellido del usuario", example = "Pérez")
        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @Schema(description = "Número de teléfono (opcional)", example = "+591 78945612")
        String phoneNumber,

        @Schema(description = "Rol del usuario", example = "CLIENTE", allowableValues = {"CLIENTE", "EMPRESA", "ADMIN"})
        @NotBlank(message = "El rol es obligatorio")
        String role
) {}
