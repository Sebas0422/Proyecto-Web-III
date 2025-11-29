package com.proyectoweb.payments.application.queries.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;

import java.util.List;
import java.util.UUID;

public record GetPaymentsByTenantQuery(
        UUID tenantId,
        String status
) implements Command<List<PaymentDto>> {
}
