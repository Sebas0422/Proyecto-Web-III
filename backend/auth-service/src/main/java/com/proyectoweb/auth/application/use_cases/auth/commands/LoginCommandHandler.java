package com.proyectoweb.auth.application.use_cases.auth.commands;

import an.awesome.pipelinr.Command;
import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.infrastructure.security.JwtTokenProvider;
import com.proyectoweb.auth.shared_kernel.core.BusinessRuleValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LoginCommandHandler implements Command.Handler<LoginCommand, LoginResponse> {

    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginCommandHandler(UserRepository userRepository, 
                               UserCompanyRepository userCompanyRepository,
                               JwtTokenProvider jwtTokenProvider,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userCompanyRepository = userCompanyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse handle(LoginCommand command) {
        Email email = new Email(command.getEmail());
        
        // Buscar usuario
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessRuleValidationException("Credenciales inválidas"));

        // Verificar que puede hacer login
        if (!user.canLogin()) {
            throw new BusinessRuleValidationException("Usuario inactivo o eliminado");
        }

        // Verificar contraseña con BCrypt
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword().getHashedValue())) {
            throw new BusinessRuleValidationException("Credenciales inválidas");
        }

        // Obtener tenant_id (company) si aplica
        UUID tenantId = getTenantId(user);

        // Generar JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), tenantId, user.getDefaultRole());

        return new LoginResponse(
            token,
            user.getId(),
            user.getEmail().getValue(),
            user.getFullName(),
            user.getDefaultRole().name(),
            tenantId
        );
    }

    private UUID getTenantId(User user) {
        // Buscar primera empresa asignada (ahora todos los roles pueden tener empresa)
        List<UserCompany> userCompanies = userCompanyRepository.findByUserId(user.getId());
        if (!userCompanies.isEmpty()) {
            // Retornar la primera empresa activa
            return userCompanies.stream()
                    .filter(UserCompany::isActive)
                    .findFirst()
                    .map(UserCompany::getCompanyId)
                    .orElse(null);
        }

        return null;
    }
}
