package com.proyectoweb.auth.application.use_cases.company.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AssignUserToCompanyCommand implements Command<Void> {
    private final UUID userId;
    private final UUID companyId;
    private final String role; // EMPRESA o ADMIN
}
