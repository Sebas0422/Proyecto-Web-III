package com.proyectoweb.proyectos.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoteAttachmentDto {
    private UUID id;
    private UUID loteId;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;

    public LoteAttachmentDto() {
    }

    public LoteAttachmentDto(UUID id, UUID loteId, String fileName, String fileType, String fileUrl, Long fileSize, String uploadedBy, LocalDateTime uploadedAt) {
        this.id = id;
        this.loteId = loteId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public void setLoteId(UUID loteId) {
        this.loteId = loteId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
