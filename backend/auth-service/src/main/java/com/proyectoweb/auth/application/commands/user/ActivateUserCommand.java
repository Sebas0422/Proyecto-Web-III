package com.proyectoweb.auth.application.commands.user;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record ActivateUserCommand(UUID userId) implements Command<Void> {}
