package com.proyectoweb.auth.application.commands.user;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record ChangePasswordCommand(
        UUID userId,
        String newPassword
) implements Command<Void> {}
