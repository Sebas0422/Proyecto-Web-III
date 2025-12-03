package com.proyectoweb.auth.application.commands.user;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record DeactivateUserCommand(UUID userId) implements Command<Void> {}
