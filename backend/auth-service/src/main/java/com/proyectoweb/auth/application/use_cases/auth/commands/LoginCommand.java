package com.proyectoweb.auth.application.use_cases.auth.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCommand implements Command<LoginResponse> {
    private final String email;
    private final String password;
}
