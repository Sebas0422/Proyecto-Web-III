package com.proyectoweb.auth.application.commands.user;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.domain.value_objects.Password;
import com.proyectoweb.auth.domain.value_objects.Role;
import an.awesome.pipelinr.Command;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateUserCommandHandler implements Command.Handler<CreateUserCommand, UUID> {

    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserCommandHandler(UserRepository userRepository,
                                    UserCompanyRepository userCompanyRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userCompanyRepository = userCompanyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UUID handle(CreateUserCommand command) {
        // Validar que el email no exista
        Email email = new Email(command.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear contraseña
        Password password = Password.fromPlainText(command.password());

        // Crear usuario
        Role role = Role.valueOf(command.role());
        User user = User.register(
                email,
                password,
                command.firstName(),
                command.lastName(),
                command.phoneNumber(),
                role
        );

        userRepository.save(user);

        // Asignar a empresa si se proporcionó companyId
        if (command.companyId() != null) {
            UserCompany userCompany = UserCompany.assign(
                    user.getId(),
                    command.companyId(),
                    role
            );
            userCompanyRepository.save(userCompany);
        }

        return user.getId();
    }
}
