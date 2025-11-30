package com.proyectoweb.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyRequest(
        @NotBlank(message = "El nombre de la empresa es obligatorio")
        String name,

        @NotBlank(message = "El RUC es obligatorio")
        String ruc,

        String address,
        String phoneNumber,
        String email
) {}
