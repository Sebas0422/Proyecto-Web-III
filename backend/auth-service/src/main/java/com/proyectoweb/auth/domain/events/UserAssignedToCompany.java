package com.proyectoweb.auth.domain.events;

import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserAssignedToCompany extends DomainEvent {
    private final UUID userId;
    private final UUID companyId;
    private final String role;

    public UserAssignedToCompany(UUID userId, UUID companyId, String role) {
        super();
        this.userId = userId;
        this.companyId = companyId;
        this.role = role;
    }
}
