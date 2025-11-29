package com.proyectoweb.proyectos.domain.events;

import com.proyectoweb.proyectos.shared_kernel.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProyectoCreated(
        UUID eventId,
        LocalDateTime occurredOn,
        UUID proyectoId,
        UUID tenantId,
        String proyectoNombre
) implements DomainEvent {
    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String getEventType() {
        return "ProyectoCreated";
    }
}
