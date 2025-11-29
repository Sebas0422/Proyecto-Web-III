package com.proyectoweb.reservations.application.queries.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;

import java.util.List;
import java.util.UUID;

public record GetLeadsByTenantQuery(
        UUID tenantId,
        String status
) implements Command<List<LeadDto>> {
}
