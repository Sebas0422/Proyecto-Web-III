package com.proyectoweb.reservations.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDto(
        UUID id,
        UUID tenantId,
        UUID lotId,
        UUID projectId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        BigDecimal reservationAmount,
        String status,
        LocalDateTime reservationDate,
        LocalDateTime expirationDate,
        LocalDateTime confirmedAt,
        LocalDateTime cancelledAt,
        String notes,
        UUID createdBy,
        LocalDateTime createdAt
) {
}
