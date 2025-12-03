package com.proyectoweb.proyectos.domain.aggregates;

import com.proyectoweb.proyectos.shared_kernel.AggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoteAttachment extends AggregateRoot {
    private UUID loteId;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;

    private LoteAttachment(UUID id) {
        super(id);
    }

    public static LoteAttachment crear(
            UUID id,
            UUID loteId,
            String fileName,
            String fileType,
            String fileUrl,
            Long fileSize,
            String uploadedBy
    ) {
        if (loteId == null) {
            throw new IllegalArgumentException("El loteId no puede ser nulo");
        }
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre de archivo no puede estar vacío");
        }
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("La URL del archivo no puede estar vacía");
        }

        LoteAttachment attachment = new LoteAttachment(id);
        attachment.loteId = loteId;
        attachment.fileName = fileName;
        attachment.fileType = fileType;
        attachment.fileUrl = fileUrl;
        attachment.fileSize = fileSize;
        attachment.uploadedBy = uploadedBy;
        attachment.uploadedAt = LocalDateTime.now();

        return attachment;
    }

    // Getters
    public UUID getLoteId() { return loteId; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public String getFileUrl() { return fileUrl; }
    public Long getFileSize() { return fileSize; }
    public String getUploadedBy() { return uploadedBy; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}
