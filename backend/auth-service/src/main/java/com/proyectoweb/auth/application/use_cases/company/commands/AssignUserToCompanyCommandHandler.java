package com.proyectoweb.auth.application.use_cases.company.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.shared_kernel.core.BusinessRuleValidationException;
import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class AssignUserToCompanyCommandHandler implements Command.Handler<AssignUserToCompanyCommand, Void> {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final Pipeline pipeline;

    public AssignUserToCompanyCommandHandler(UserRepository userRepository,
                                            CompanyRepository companyRepository,
                                            UserCompanyRepository userCompanyRepository,
                                            Pipeline pipeline) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.userCompanyRepository = userCompanyRepository;
        this.pipeline = pipeline;
    }

    @Override
    public Void handle(AssignUserToCompanyCommand command) {
        // Validar que usuario y empresa existen
        userRepository.findById(command.getUserId())
            .orElseThrow(() -> new BusinessRuleValidationException("Usuario no encontrado"));
        
        companyRepository.findById(command.getCompanyId())
            .orElseThrow(() -> new BusinessRuleValidationException("Empresa no encontrada"));

        // Validar que no esté ya asignado
        if (userCompanyRepository.existsByUserIdAndCompanyId(
                command.getUserId(), command.getCompanyId())) {
            throw new BusinessRuleValidationException("El usuario ya está asignado a esta empresa");
        }

        // Parsear rol
        Role role = parseRole(command.getRole());

        // Crear asignación
        UserCompany userCompany = UserCompany.assign(
            command.getUserId(),
            command.getCompanyId(),
            role
        );

        // Guardar
        UserCompany saved = userCompanyRepository.save(userCompany);

        // Publicar eventos
        for (DomainEvent event : saved.getDomainEvents()) {
            pipeline.send(event);
        }
        saved.clearDomainEvents();

        return null;
    }

    private Role parseRole(String roleStr) {
        try {
            Role role = Role.valueOf(roleStr.toUpperCase());
            if (role == Role.CLIENTE) {
                throw new BusinessRuleValidationException(
                    "No se puede asignar el rol CLIENTE a una empresa");
            }
            return role;
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleValidationException("Rol inválido: " + roleStr);
        }
    }
}
