package com.proyectoweb.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateUserRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        String phoneNumber,

        String role,

        UUID companyId
) {}
