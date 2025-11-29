package com.proyectoweb.auth.application.use_cases.company.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import com.proyectoweb.auth.shared_kernel.core.BusinessRuleValidationException;
import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateCompanyCommandHandler implements Command.Handler<CreateCompanyCommand, UUID> {

    private final CompanyRepository companyRepository;
    private final Pipeline pipeline;

    public CreateCompanyCommandHandler(CompanyRepository companyRepository, Pipeline pipeline) {
        this.companyRepository = companyRepository;
        this.pipeline = pipeline;
    }

    @Override
    public UUID handle(CreateCompanyCommand command) {
        // Verificar que el RUC no exista
        if (companyRepository.existsByRuc(command.getRuc())) {
            throw new BusinessRuleValidationException("El RUC ya está registrado");
        }

        // Crear empresa
        Company company = Company.create(
            command.getName(),
            command.getRuc(),
            command.getAddress(),
            command.getPhoneNumber(),
            command.getEmail()
        );

        // Guardar
        Company savedCompany = companyRepository.save(company);

        // Publicar eventos
        for (DomainEvent event : savedCompany.getDomainEvents()) {
            pipeline.send(event);
        }
        savedCompany.clearDomainEvents();

        return savedCompany.getId();
    }
}
