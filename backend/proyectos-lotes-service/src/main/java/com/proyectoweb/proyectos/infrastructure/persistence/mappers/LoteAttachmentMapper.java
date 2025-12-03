package com.proyectoweb.proyectos.infrastructure.persistence.mappers;

import com.proyectoweb.proyectos.domain.aggregates.LoteAttachment;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.LoteAttachmentJpaModel;
import org.springframework.stereotype.Component;

@Component
public class LoteAttachmentMapper {

    public LoteAttachmentJpaModel toJpa(LoteAttachment attachment) {
        LoteAttachmentJpaModel jpa = new LoteAttachmentJpaModel();
        jpa.setId(attachment.getId());
        jpa.setLoteId(attachment.getLoteId());
        jpa.setFileName(attachment.getFileName());
        jpa.setFileType(attachment.getFileType());
        jpa.setFileUrl(attachment.getFileUrl());
        jpa.setFileSize(attachment.getFileSize());
        jpa.setUploadedBy(attachment.getUploadedBy());
        jpa.setUploadedAt(attachment.getUploadedAt());
        return jpa;
    }

    public LoteAttachment toDomain(LoteAttachmentJpaModel jpa) {
        return LoteAttachment.crear(
                jpa.getId(),
                jpa.getLoteId(),
                jpa.getFileName(),
                jpa.getFileType(),
                jpa.getFileUrl(),
                jpa.getFileSize(),
                jpa.getUploadedBy()
        );
    }
}
