package com.proyectoweb.proyectos.application.queries.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;

import java.util.UUID;

public class GetLoteByIdQuery implements Command<LoteDto> {
    private final UUID loteId;
    private final UUID tenantId;

    public GetLoteByIdQuery(UUID loteId, UUID tenantId) {
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
