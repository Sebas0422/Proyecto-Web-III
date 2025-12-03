package com.proyectoweb.proyectos.application.commands.attachment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteAttachmentDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.LoteAttachment;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteAttachmentRepository;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.services.FileStorageService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UploadAttachmentCommandHandler implements Command.Handler<UploadAttachmentCommand, LoteAttachmentDto> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;
    private final LoteAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public UploadAttachmentCommandHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository, 
                                         LoteAttachmentRepository attachmentRepository, FileStorageService fileStorageService) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public LoteAttachmentDto handle(UploadAttachmentCommand command) {
        try {
            // Validar lote y tenant
            Lote lote = loteRepository.findById(command.getLoteId())
                    .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

            Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

            if (!proyecto.getTenantId().equals(command.getTenantId())) {
                throw new RuntimeException("No tiene permisos para subir archivos a este lote");
            }

            // Guardar archivo
            String fileUrl = fileStorageService.storeFile(command.getFile(), command.getLoteId());

            // Crear attachment
            LoteAttachment attachment = LoteAttachment.crear(
                    UUID.randomUUID(),
                    command.getLoteId(),
                    command.getFile().getOriginalFilename(),
                    command.getFile().getContentType(),
                    fileStorageService.getFileUrl(fileUrl),
                    command.getFile().getSize(),
                    command.getUploadedBy()
            );

            LoteAttachment saved = attachmentRepository.save(attachment);

            return new LoteAttachmentDto(
                    saved.getId(),
                    saved.getLoteId(),
                    saved.getFileName(),
                    saved.getFileType(),
                    saved.getFileUrl(),
                    saved.getFileSize(),
                    saved.getUploadedBy(),
                    saved.getUploadedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al subir archivo: " + e.getMessage(), e);
        }
    }
}
