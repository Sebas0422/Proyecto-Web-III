package com.proyectoweb.proyectos.application.commands.attachment;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.LoteAttachment;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteAttachmentRepository;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.services.FileStorageService;
import org.springframework.stereotype.Component;

@Component
public class DeleteAttachmentCommandHandler implements Command.Handler<DeleteAttachmentCommand, Voidy> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;
    private final LoteAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public DeleteAttachmentCommandHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository, 
                                         LoteAttachmentRepository attachmentRepository, FileStorageService fileStorageService) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Voidy handle(DeleteAttachmentCommand command) {
        try {
            LoteAttachment attachment = attachmentRepository.findById(command.getAttachmentId())
                    .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

            // Validar lote y tenant
            Lote lote = loteRepository.findById(attachment.getLoteId())
                    .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

            Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

            if (!proyecto.getTenantId().equals(command.getTenantId())) {
                throw new RuntimeException("No tiene permisos para eliminar archivos de este lote");
            }

            // Eliminar archivo del sistema de archivos
            String fileUrl = attachment.getFileUrl().replace("/api/lotes/files/", "");
            fileStorageService.deleteFile(fileUrl);

            // Eliminar registro
            attachmentRepository.deleteById(command.getAttachmentId());

            return new Voidy();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar archivo: " + e.getMessage(), e);
        }
    }
}
