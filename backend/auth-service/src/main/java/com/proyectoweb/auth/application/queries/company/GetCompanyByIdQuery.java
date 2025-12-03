package com.proyectoweb.auth.application.queries.company;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.application.use_cases.company.queries.CompanyDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GetCompanyByIdQuery implements Command<CompanyDto> {
    private final UUID companyId;
}
