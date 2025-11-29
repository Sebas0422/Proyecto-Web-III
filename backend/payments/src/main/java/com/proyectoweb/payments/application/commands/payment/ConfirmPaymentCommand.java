package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;

import java.util.UUID;

public record ConfirmPaymentCommand(
        UUID paymentId,
        UUID tenantId,
        String transactionReference
) implements Command<PaymentDto> {
}
