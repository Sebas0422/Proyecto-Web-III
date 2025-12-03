package com.proyectoweb.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "La nueva contraseña es obligatoria")
        String newPassword
) {}
