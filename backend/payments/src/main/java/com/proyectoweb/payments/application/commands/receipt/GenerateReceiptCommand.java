package com.proyectoweb.payments.application.commands.receipt;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.ReceiptDto;

import java.util.UUID;

public record GenerateReceiptCommand(
        UUID paymentId,
        UUID tenantId,
        UUID issuedBy
) implements Command<ReceiptDto> {
}
