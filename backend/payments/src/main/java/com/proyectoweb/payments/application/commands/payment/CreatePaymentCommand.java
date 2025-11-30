package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.domain.value_objects.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentCommand(
        UUID tenantId,
        UUID reservationId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        BigDecimal amount,
        String currency,
        PaymentMethod paymentMethod,
        int expirationHours,
        UUID createdBy,
        String notes
) implements Command<PaymentDto> {
}
