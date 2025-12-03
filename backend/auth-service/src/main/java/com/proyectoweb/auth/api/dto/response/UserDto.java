package com.proyectoweb.auth.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String role,
        boolean isActive,
        boolean isEmailVerified,
        UUID companyId,
        String companyName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
