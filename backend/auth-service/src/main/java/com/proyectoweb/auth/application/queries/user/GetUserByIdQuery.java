package com.proyectoweb.auth.application.queries.user;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.api.dto.response.UserDto;

import java.util.UUID;

public record GetUserByIdQuery(UUID userId) implements Command<UserDto> {}
