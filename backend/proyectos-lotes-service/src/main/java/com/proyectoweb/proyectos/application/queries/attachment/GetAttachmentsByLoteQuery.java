package com.proyectoweb.proyectos.application.queries.attachment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteAttachmentDto;

import java.util.List;
import java.util.UUID;

public class GetAttachmentsByLoteQuery implements Command<List<LoteAttachmentDto>> {
    private final UUID loteId;
    private final UUID tenantId;

    public GetAttachmentsByLoteQuery(UUID loteId, UUID tenantId) {
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
