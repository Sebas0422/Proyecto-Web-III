package com.proyectoweb.proyectos.application.queries.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;

import java.util.UUID;

public class GetProyectoByIdQuery implements Command<ProyectoDto> {
    private final UUID proyectoId;
    private final UUID tenantId;

    public GetProyectoByIdQuery(UUID proyectoId, UUID tenantId) {
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
