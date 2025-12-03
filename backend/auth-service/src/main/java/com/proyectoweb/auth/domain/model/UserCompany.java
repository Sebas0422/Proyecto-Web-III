package com.proyectoweb.auth.domain.model;

import com.proyectoweb.auth.domain.events.UserAssignedToCompany;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.shared_kernel.core.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserCompany extends Entity {
    private UUID userId;
    private UUID companyId;
    private Role role;
    private boolean isActive;

    // Constructor privado
    private UserCompany(UUID id, UUID userId, UUID companyId, Role role) {
        super(id);
        this.userId = userId;
        this.companyId = companyId;
        this.role = role;
        this.isActive = true;
    }

    // Factory method
    public static UserCompany assign(UUID userId, UUID companyId, Role role) {
        validateAssignment(userId, companyId, role);
        
        UUID assignmentId = UUID.randomUUID();
        UserCompany userCompany = new UserCompany(assignmentId, userId, companyId, role);
        
        userCompany.addDomainEvent(new UserAssignedToCompany(userId, companyId, role.name()));
        
        return userCompany;
    }

    // Reconstituir desde BD
    public static UserCompany reconstitute(UUID id, UUID userId, UUID companyId, 
                                          Role role, boolean isActive) {
        UserCompany userCompany = new UserCompany(id, userId, companyId, role);
        userCompany.isActive = isActive;
        return userCompany;
    }

    // Métodos de negocio
    public void changeRole(Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.role = newRole;
        updateTimestamp();
    }

    public void activate() {
        this.isActive = true;
        updateTimestamp();
    }

    public void deactivate() {
        this.isActive = false;
        updateTimestamp();
    }

    // Validaciones
    private static void validateAssignment(UUID userId, UUID companyId, Role role) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId no puede ser nulo");
        }
        if (companyId == null) {
            throw new IllegalArgumentException("CompanyId no puede ser nulo");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role no puede ser nulo");
        }
        // Permitir asignar cualquier rol (CLIENTE, EMPRESA, ADMIN) a una empresa
    }
}
