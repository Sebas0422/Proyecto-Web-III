package com.proyectoweb.auth.application.queries.user;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.api.dto.response.UserDto;

import java.util.List;

public record GetAllUsersQuery(
        String role,      // Filtro opcional por rol
        Boolean isActive  // Filtro opcional por estado
) implements Command<List<UserDto>> {}
