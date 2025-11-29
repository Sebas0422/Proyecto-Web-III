package com.proyectoweb.auth.application.use_cases.company.queries;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllCompaniesQuery implements Command<List<CompanyDto>> {
    private final boolean onlyActive;
}
