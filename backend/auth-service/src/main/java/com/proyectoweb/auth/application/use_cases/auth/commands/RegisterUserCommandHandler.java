package com.proyectoweb.auth.application.use_cases.auth.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.domain.value_objects.Password;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.shared_kernel.core.BusinessRuleValidationException;
import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserCommandHandler implements Command.Handler<RegisterUserCommand, RegisterUserResponse> {

    private final UserRepository userRepository;
    private final Pipeline pipeline;

    public RegisterUserCommandHandler(UserRepository userRepository, Pipeline pipeline) {
        this.userRepository = userRepository;
        this.pipeline = pipeline;
    }

    @Override
    public RegisterUserResponse handle(RegisterUserCommand command) {
        // Validar datos
        Email email = new Email(command.getEmail());
        Password password = Password.fromPlainText(command.getPassword());
        Role role = parseRole(command.getRole());

        // Verificar que el email no exista
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleValidationException("El email ya está registrado");
        }

        // Crear usuario (agregate root)
        User user = User.register(
            email,
            password,
            command.getFirstName(),
            command.getLastName(),
            command.getPhoneNumber(),
            role
        );

        // Guardar en BD
        User savedUser = userRepository.save(user);

        // Publicar eventos de dominio
        for (DomainEvent event : savedUser.getDomainEvents()) {
            pipeline.send(event);
        }
        savedUser.clearDomainEvents();

        return new RegisterUserResponse(
            savedUser.getId(),
            savedUser.getEmail().getValue(),
            savedUser.getFullName(),
            savedUser.getDefaultRole().name(),
            "Usuario registrado exitosamente"
        );
    }

    private Role parseRole(String roleStr) {
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleValidationException("Rol inválido: " + roleStr);
        }
    }
}
