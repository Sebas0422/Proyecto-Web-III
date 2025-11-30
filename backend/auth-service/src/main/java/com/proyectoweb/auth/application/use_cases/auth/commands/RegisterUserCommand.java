package com.proyectoweb.auth.application.use_cases.auth.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserCommand implements Command<RegisterUserResponse> {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String role; // CLIENTE, EMPRESA, ADMIN
}
