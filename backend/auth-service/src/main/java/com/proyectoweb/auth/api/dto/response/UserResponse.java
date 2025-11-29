package com.proyectoweb.auth.api.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String email,
        String fullName,
        String role,
        String message
) {}
