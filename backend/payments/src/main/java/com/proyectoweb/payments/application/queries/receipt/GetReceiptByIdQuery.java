package com.proyectoweb.payments.application.queries.receipt;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.ReceiptDto;

import java.util.UUID;

public record GetReceiptByIdQuery(
        UUID receiptId,
        UUID tenantId
) implements Command<ReceiptDto> {
}
