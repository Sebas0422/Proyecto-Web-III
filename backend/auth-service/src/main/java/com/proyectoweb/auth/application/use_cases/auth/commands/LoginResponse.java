package com.proyectoweb.auth.application.use_cases.auth.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final String token;
    private final UUID userId;
    private final String email;
    private final String fullName;
    private final String role;
    private final UUID tenantId; // null para clientes, companyId para empresa/admin
}
