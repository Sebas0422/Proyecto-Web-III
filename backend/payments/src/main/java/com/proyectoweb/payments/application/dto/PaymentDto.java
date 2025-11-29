package com.proyectoweb.payments.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDto(
        UUID id,
        UUID tenantId,
        UUID reservationId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String status,
        String transactionReference,
        String qrCodeData,
        LocalDateTime paymentDate,
        LocalDateTime expirationDate,
        LocalDateTime confirmedAt,
        String notes,
        UUID createdBy,
        LocalDateTime createdAt
) {
}
