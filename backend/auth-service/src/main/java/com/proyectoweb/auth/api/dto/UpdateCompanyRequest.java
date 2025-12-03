package com.proyectoweb.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String name,
        
        String address,
        
        String phoneNumber,
        
        @Email(message = "El email debe ser válido")
        String email,
        
        String logoUrl
) {
}
