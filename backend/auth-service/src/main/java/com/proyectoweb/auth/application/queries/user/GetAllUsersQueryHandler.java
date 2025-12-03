package com.proyectoweb.auth.application.queries.user;

import com.proyectoweb.auth.api.dto.response.UserDto;
import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetAllUsersQueryHandler implements Command.Handler<GetAllUsersQuery, List<UserDto>> {

    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final CompanyRepository companyRepository;

    public GetAllUsersQueryHandler(UserRepository userRepository,
                                   UserCompanyRepository userCompanyRepository,
                                   CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.userCompanyRepository = userCompanyRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<UserDto> handle(GetAllUsersQuery query) {
        List<User> users = userRepository.findAll();

        // Aplicar filtros
        if (query.role() != null && !query.role().isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getDefaultRole().name().equals(query.role()))
                    .collect(Collectors.toList());
        }

        if (query.isActive() != null) {
            users = users.stream()
                    .filter(user -> user.isActive() == query.isActive())
                    .collect(Collectors.toList());
        }

        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private UserDto toDto(User user) {
        // Obtener la empresa asignada (si existe)
        List<UserCompany> userCompanies = userCompanyRepository.findByUserId(user.getId());
        UUID companyId = null;
        String companyName = null;
        
        if (!userCompanies.isEmpty()) {
            // Tomar la primera empresa activa
            UserCompany activeAssignment = userCompanies.stream()
                    .filter(UserCompany::isActive)
                    .findFirst()
                    .orElse(null);
            
            if (activeAssignment != null) {
                companyId = activeAssignment.getCompanyId();
                companyName = companyRepository.findById(companyId)
                        .map(company -> company.getName())
                        .orElse(null);
            }
        }
        
        return new UserDto(
                user.getId(),
                user.getEmail().getValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDefaultRole().name(),
                user.isActive(),
                user.isEmailVerified(),
                companyId,
                companyName,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
