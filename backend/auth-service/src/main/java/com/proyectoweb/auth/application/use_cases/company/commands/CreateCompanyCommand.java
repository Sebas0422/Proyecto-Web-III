package com.proyectoweb.auth.application.use_cases.company.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateCompanyCommand implements Command<UUID> {
    private final String name;
    private final String ruc;
    private final String address;
    private final String phoneNumber;
    private final String email;
}
