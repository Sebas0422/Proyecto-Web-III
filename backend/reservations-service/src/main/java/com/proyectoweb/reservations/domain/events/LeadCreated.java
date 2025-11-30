package com.proyectoweb.reservations.domain.events;

import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.shared_kernel.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record LeadCreated(
        UUID leadId,
        UUID tenantId,
        CustomerInfo customerInfo,
        String source,
        LocalDateTime occurredOn
) implements DomainEvent {
}
