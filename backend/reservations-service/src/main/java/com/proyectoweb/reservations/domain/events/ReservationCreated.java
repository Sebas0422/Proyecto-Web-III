package com.proyectoweb.reservations.domain.events;

import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.shared_kernel.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationCreated(
        UUID reservationId,
        UUID tenantId,
        UUID lotId,
        UUID projectId,
        CustomerInfo customerInfo,
        BigDecimal reservationAmount,
        LocalDateTime occurredOn
) implements DomainEvent {
}
