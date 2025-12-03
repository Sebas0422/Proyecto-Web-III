package com.proyectoweb.auth.application.queries.company;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.application.use_cases.company.queries.CompanyDto;
import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCompanyByIdQueryHandler implements Command.Handler<GetCompanyByIdQuery, CompanyDto> {
    
    private final CompanyRepository companyRepository;

    @Override
    public CompanyDto handle(GetCompanyByIdQuery query) {
        Company company = companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

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
