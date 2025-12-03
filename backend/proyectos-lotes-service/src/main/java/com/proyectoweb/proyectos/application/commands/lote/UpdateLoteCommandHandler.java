package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import com.proyectoweb.proyectos.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LoteUpdatedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UpdateLoteCommandHandler implements Command.Handler<UpdateLoteCommand, LoteDto> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;
    private final KafkaProducerService kafkaProducerService;

    public UpdateLoteCommandHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository, KafkaProducerService kafkaProducerService) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public LoteDto handle(UpdateLoteCommand command) {
        Lote lote = loteRepository.findById(command.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Validar que el proyecto pertenece al tenant
        Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No tiene permisos para actualizar este lote");
        }

        // Actualizar precio si se proporciona
        if (command.getPrecio() != null) {
            lote.updatePrecio(new Precio(command.getPrecio()));
        }

        // Actualizar estado si se proporciona
        if (command.getEstado() != null) {
            EstadoLote nuevoEstado = EstadoLote.valueOf(command.getEstado().toUpperCase());
            lote.updateEstado(nuevoEstado);
        }

        // Actualizar observaciones
        if (command.getObservaciones() != null) {
            lote.updateObservaciones(command.getObservaciones());
        }

        Lote savedLote = loteRepository.save(lote);

        // Publicar evento
        LoteUpdatedEvent event = new LoteUpdatedEvent(
                UUID.randomUUID(),
                LocalDateTime.now(),
                savedLote.getId(),
                proyecto.getTenantId().toString(),
                savedLote.getProyectoId(),
                savedLote.getNumeroLote(),
                savedLote.getPrecio().value(),
                savedLote.getEstado().name()
        );
        kafkaProducerService.publishLoteUpdated(event);

        return new LoteDto(
                savedLote.getId(),
                savedLote.getProyectoId(),
                savedLote.getNumeroLote(),
                savedLote.getManzana(),
                savedLote.getGeometria().toWKT(),
                savedLote.getAreaCalculada(),
                savedLote.getCentroide().toText(),
                savedLote.getPrecio().value(),
                savedLote.getEstado().name(),
                savedLote.getObservaciones(),
                savedLote.getCreatedAt()
        );
    }
}
