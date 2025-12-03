package com.proyectoweb.auth.application.commands.user;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record CreateUserCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        String role,
        UUID companyId
) implements Command<UUID> {}
