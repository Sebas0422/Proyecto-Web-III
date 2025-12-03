package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LoteDeletedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DeleteLoteCommandHandler implements Command.Handler<DeleteLoteCommand, Voidy> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;
    private final KafkaProducerService kafkaProducerService;

    public DeleteLoteCommandHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository, KafkaProducerService kafkaProducerService) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Voidy handle(DeleteLoteCommand command) {
        Lote lote = loteRepository.findById(command.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Validar que el proyecto pertenece al tenant
        Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No tiene permisos para eliminar este lote");
        }

        // Soft delete: marcar como NO_DISPONIBLE
        lote.marcarComoNoDisponible();
        loteRepository.save(lote);

        // Publicar evento
        LoteDeletedEvent event = new LoteDeletedEvent(
                UUID.randomUUID(),
                LocalDateTime.now(),
                lote.getId(),
                proyecto.getTenantId().toString(),
                lote.getProyectoId(),
                lote.getNumeroLote()
        );
        kafkaProducerService.publishLoteDeleted(event);

        return new Voidy();
    }
}
