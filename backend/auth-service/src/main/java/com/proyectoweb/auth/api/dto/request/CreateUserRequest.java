package com.proyectoweb.auth.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateUserRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        String phoneNumber,

        @NotBlank(message = "El rol es obligatorio")
        @Pattern(regexp = "ADMIN|EMPRESA|CLIENTE", message = "Rol inválido")
        String role,

        UUID companyId
) {}
