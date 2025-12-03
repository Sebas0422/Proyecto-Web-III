package com.proyectoweb.auth.application.commands.user;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.domain.value_objects.Role;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UpdateUserCommandHandler implements Command.Handler<UpdateUserCommand, Void> {

    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;

    public UpdateUserCommandHandler(UserRepository userRepository,
                                    UserCompanyRepository userCompanyRepository) {
        this.userRepository = userRepository;
        this.userCompanyRepository = userCompanyRepository;
    }

    @Override
    @Transactional
    public Void handle(UpdateUserCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar perfil
        user.updateProfile(
                command.firstName(),
                command.lastName(),
                command.phoneNumber()
        );

        // Actualizar rol si se proporciona
        if (command.role() != null && !command.role().isEmpty()) {
            Role newRole = Role.valueOf(command.role());
            user.changeRole(newRole);
        }

        userRepository.save(user);

        // Gestionar asignación de empresa
        if (command.companyId() != null) {
            // Verificar si ya existe una asignación a esta empresa
            List<UserCompany> existingAssignments = userCompanyRepository.findByUserId(command.userId());
            
            UserCompany targetAssignment = existingAssignments.stream()
                    .filter(a -> a.getCompanyId().equals(command.companyId()))
                    .findFirst()
                    .orElse(null);
            
            Role role = command.role() != null ? Role.valueOf(command.role()) : user.getDefaultRole();
            
            if (targetAssignment != null) {
                // Ya existe asignación a esta empresa - actualizarla
                if (!targetAssignment.isActive()) {
                    targetAssignment.activate();
                }
                if (!targetAssignment.getRole().equals(role)) {
                    targetAssignment.changeRole(role);
                }
                userCompanyRepository.save(targetAssignment);
                
                // Desactivar otras asignaciones
                existingAssignments.stream()
                        .filter(a -> !a.getCompanyId().equals(command.companyId()))
                        .forEach(a -> {
                            a.deactivate();
                            userCompanyRepository.save(a);
                        });
            } else {
                // No existe asignación a esta empresa - crear nueva
                // Primero desactivar todas las asignaciones existentes
                existingAssignments.forEach(assignment -> {
                    assignment.deactivate();
                    userCompanyRepository.save(assignment);
                });
                
                // Crear nueva asignación
                UserCompany newAssignment = UserCompany.assign(
                        command.userId(),
                        command.companyId(),
                        role
                );
                userCompanyRepository.save(newAssignment);
            }
        } else {
            // Si companyId es null, desactivar todas las asignaciones
            List<UserCompany> existingAssignments = userCompanyRepository.findByUserId(command.userId());
            existingAssignments.forEach(assignment -> {
                assignment.deactivate();
                userCompanyRepository.save(assignment);
            });
        }

        return null;
    }
}
