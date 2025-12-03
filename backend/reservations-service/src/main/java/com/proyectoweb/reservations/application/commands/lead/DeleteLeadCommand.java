package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public class DeleteLeadCommand implements Command<Voidy> {
    private final UUID leadId;
    private final UUID tenantId;

    public DeleteLeadCommand(UUID leadId, UUID tenantId) {
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
