package com.proyectoweb.auth.application.use_cases.company.queries;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetAllCompaniesQueryHandler implements Command.Handler<GetAllCompaniesQuery, List<CompanyDto>> {

    private final CompanyRepository companyRepository;

    public GetAllCompaniesQueryHandler(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDto> handle(GetAllCompaniesQuery query) {
        List<Company> companies = query.isOnlyActive() 
            ? companyRepository.findAllActive()
            : companyRepository.findAll();

        return companies.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private CompanyDto toDto(Company company) {
        return new CompanyDto(
            company.getId(),
            company.getName(),
            company.getRuc(),
            company.getAddress(),
            company.getPhoneNumber(),
            company.getEmail(),
            company.getLogoUrl(),
            company.isActive()
        );
    }
}
