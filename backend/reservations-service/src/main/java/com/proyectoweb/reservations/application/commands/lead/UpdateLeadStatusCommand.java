package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;

import java.util.UUID;

public class UpdateLeadStatusCommand implements Command<LeadDto> {
    private final UUID leadId;
    private final UUID tenantId;
    private final String status;

    public UpdateLeadStatusCommand(UUID leadId, UUID tenantId, String status) {
        this.leadId = leadId;
        this.tenantId = tenantId;
        this.status = status;
    }

    public UUID getLeadId() {
        return leadId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getStatus() {
        return status;
    }
}
