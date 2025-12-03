package com.proyectoweb.auth.application.commands.company;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeactivateCompanyCommand implements Command<Voidy> {
    private final UUID companyId;
}
