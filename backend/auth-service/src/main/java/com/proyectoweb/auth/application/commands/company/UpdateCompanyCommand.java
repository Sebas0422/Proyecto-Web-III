package com.proyectoweb.auth.application.commands.company;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.application.use_cases.company.queries.CompanyDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UpdateCompanyCommand implements Command<CompanyDto> {
    private final UUID companyId;
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String email;
    private final String logoUrl;
}
