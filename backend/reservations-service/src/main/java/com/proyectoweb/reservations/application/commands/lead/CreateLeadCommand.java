package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;

import java.util.UUID;

public record CreateLeadCommand(
        UUID tenantId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        String source,
        String interestLevel,
        String notes,
        UUID projectId,
        UUID lotId
) implements Command<LeadDto> {
}
