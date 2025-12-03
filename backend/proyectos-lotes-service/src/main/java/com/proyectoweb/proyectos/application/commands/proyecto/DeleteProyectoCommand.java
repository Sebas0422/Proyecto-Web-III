package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public class DeleteProyectoCommand implements Command<Voidy> {
    private final UUID proyectoId;
    private final UUID tenantId;

    public DeleteProyectoCommand(UUID proyectoId, UUID tenantId) {
        this.proyectoId = proyectoId;
        this.tenantId = tenantId;
    }

    public UUID getProyectoId() {
        return proyectoId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
