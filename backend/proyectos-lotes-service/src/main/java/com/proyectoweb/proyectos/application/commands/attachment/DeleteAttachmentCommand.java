package com.proyectoweb.proyectos.application.commands.attachment;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public class DeleteAttachmentCommand implements Command<Voidy> {
    private final UUID attachmentId;
    private final UUID tenantId;

    public DeleteAttachmentCommand(UUID attachmentId, UUID tenantId) {
        this.attachmentId = attachmentId;
        this.tenantId = tenantId;
    }

    public UUID getAttachmentId() {
        return attachmentId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
