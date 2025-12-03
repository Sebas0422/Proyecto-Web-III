package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectDeletedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeleteProyectoCommandHandler implements Command.Handler<DeleteProyectoCommand, Voidy> {

    private final ProyectoRepository proyectoRepository;
    private final KafkaProducerService kafkaProducerService;

    public DeleteProyectoCommandHandler(ProyectoRepository proyectoRepository, KafkaProducerService kafkaProducerService) {
        this.proyectoRepository = proyectoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Voidy handle(DeleteProyectoCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No tiene permisos para eliminar este proyecto");
        }

        proyecto.deactivate();
        proyectoRepository.save(proyecto);

        ProjectDeletedEvent kafkaEvent = new ProjectDeletedEvent(
            proyecto.getId().toString(),
            proyecto.getTenantId().toString(),
            proyecto.getNombre().value(),
            LocalDateTime.now()
        );
        kafkaProducerService.publishProjectDeleted(kafkaEvent);

        return new Voidy();
    }
}
