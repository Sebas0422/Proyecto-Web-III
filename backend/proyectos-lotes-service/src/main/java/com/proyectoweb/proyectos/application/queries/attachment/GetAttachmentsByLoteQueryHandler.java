package com.proyectoweb.proyectos.application.queries.attachment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteAttachmentDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteAttachmentRepository;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetAttachmentsByLoteQueryHandler implements Command.Handler<GetAttachmentsByLoteQuery, List<LoteAttachmentDto>> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;
    private final LoteAttachmentRepository attachmentRepository;

    public GetAttachmentsByLoteQueryHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository, 
                                           LoteAttachmentRepository attachmentRepository) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public List<LoteAttachmentDto> handle(GetAttachmentsByLoteQuery query) {
        // Validar lote y tenant
        Lote lote = loteRepository.findById(query.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(query.getTenantId())) {
            throw new RuntimeException("No tiene permisos para ver archivos de este lote");
        }

        return attachmentRepository.findByLoteId(query.getLoteId()).stream()
                .map(a -> new LoteAttachmentDto(
                        a.getId(),
                        a.getLoteId(),
                        a.getFileName(),
                        a.getFileType(),
                        a.getFileUrl(),
                        a.getFileSize(),
                        a.getUploadedBy(),
                        a.getUploadedAt()
                ))
                .collect(Collectors.toList());
    }
}
