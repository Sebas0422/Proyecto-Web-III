package com.proyectoweb.reservations.application.queries.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;

import java.util.UUID;

public class GetLeadByIdQuery implements Command<LeadDto> {
    private final UUID leadId;
    private final UUID tenantId;

    public GetLeadByIdQuery(UUID leadId, UUID tenantId) {
        this.leadId = leadId;
        this.tenantId = tenantId;
    }

    public UUID getLeadId() {
        return leadId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
