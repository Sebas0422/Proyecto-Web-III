package com.proyectoweb.payments.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptDto(
        UUID id,
        UUID tenantId,
        UUID paymentId,
        String receiptNumber,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String transactionReference,
        String pdfPath,
        LocalDateTime issuedAt,
        String notes,
        UUID issuedBy,
        LocalDateTime createdAt
) {
}
