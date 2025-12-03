package com.proyectoweb.proyectos.application.commands.attachment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteAttachmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class UploadAttachmentCommand implements Command<LoteAttachmentDto> {
    private final UUID loteId;
    private final UUID tenantId;
    private final MultipartFile file;
    private final String uploadedBy;

    public UploadAttachmentCommand(UUID loteId, UUID tenantId, MultipartFile file, String uploadedBy) {
        this.loteId = loteId;
        this.tenantId = tenantId;
        this.file = file;
        this.uploadedBy = uploadedBy;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }
}
