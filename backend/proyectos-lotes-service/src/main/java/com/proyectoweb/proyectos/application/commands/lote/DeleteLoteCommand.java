package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public class DeleteLoteCommand implements Command<Voidy> {
    private final UUID loteId;
    private final UUID tenantId;

    public DeleteLoteCommand(UUID loteId, UUID tenantId) {
        this.loteId = loteId;
        this.tenantId = tenantId;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
