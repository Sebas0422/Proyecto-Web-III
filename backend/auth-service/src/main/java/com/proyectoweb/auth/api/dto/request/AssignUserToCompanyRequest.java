package com.proyectoweb.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignUserToCompanyRequest(
        @NotNull(message = "El userId es obligatorio")
        UUID userId,

        @NotNull(message = "El companyId es obligatorio")
        UUID companyId,

        @NotBlank(message = "El role es obligatorio")
        String role
) {}
