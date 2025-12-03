package com.proyectoweb.auth.application.commands.company;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeactivateCompanyCommandHandler implements Command.Handler<DeactivateCompanyCommand, Voidy> {
    
    private final CompanyRepository companyRepository;

    @Override
    public Voidy handle(DeactivateCompanyCommand command) {
        Company company = companyRepository.findById(command.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Deactivate company using domain method
        company.deactivate();

        // Save changes
        companyRepository.save(company);

        return new Voidy();
    }
}
