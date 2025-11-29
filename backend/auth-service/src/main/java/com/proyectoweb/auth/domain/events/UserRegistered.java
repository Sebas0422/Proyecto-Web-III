package com.proyectoweb.auth.domain.events;

import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserRegistered extends DomainEvent {
    private final UUID userId;
    private final String email;
    private final String role;

    public UserRegistered(UUID userId, String email, String role) {
        super();
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
