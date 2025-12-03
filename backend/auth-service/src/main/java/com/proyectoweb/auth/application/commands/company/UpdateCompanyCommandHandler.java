package com.proyectoweb.auth.application.commands.company;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.application.use_cases.company.queries.CompanyDto;
import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCompanyCommandHandler implements Command.Handler<UpdateCompanyCommand, CompanyDto> {
    
    private final CompanyRepository companyRepository;

    @Override
    public CompanyDto handle(UpdateCompanyCommand command) {
        Company company = companyRepository.findById(command.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Update company info using domain method
        company.updateInfo(
                command.getName(),
                command.getAddress(),
                command.getPhoneNumber(),
                command.getEmail()
        );

        // Update logo if provided
        if (command.getLogoUrl() != null) {
            company.updateLogo(command.getLogoUrl());
        }

        // Save changes
        Company updatedCompany = companyRepository.save(company);

        return new CompanyDto(
                updatedCompany.getId(),
                updatedCompany.getName(),
                updatedCompany.getRuc(),
                updatedCompany.getAddress(),
                updatedCompany.getPhoneNumber(),
                updatedCompany.getEmail(),
                updatedCompany.getLogoUrl(),
                updatedCompany.isActive()
        );
    }
}
