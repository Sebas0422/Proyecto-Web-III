package com.proyectoweb.reservations.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LeadDto(
        UUID id,
        UUID tenantId,
        UUID projectId,
        UUID lotId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        String status,
        String source,
        String interestLevel,
        String notes,
        UUID assignedTo,
        LocalDateTime contactedAt,
        LocalDateTime convertedAt,
        LocalDateTime createdAt
) {
}
