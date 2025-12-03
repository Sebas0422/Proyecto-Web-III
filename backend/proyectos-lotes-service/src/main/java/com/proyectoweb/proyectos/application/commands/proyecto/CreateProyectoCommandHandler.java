package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import com.proyectoweb.proyectos.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectCreatedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreateProyectoCommandHandler implements Command.Handler<CreateProyectoCommand, ProyectoDto> {
    
    private final ProyectoRepository proyectoRepository;
    private final KafkaProducerService kafkaProducerService;

    public CreateProyectoCommandHandler(ProyectoRepository proyectoRepository, 
                                        KafkaProducerService kafkaProducerService) {
        this.proyectoRepository = proyectoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public ProyectoDto handle(CreateProyectoCommand command) {
        Proyecto proyecto = Proyecto.crear(
                UUID.randomUUID(),
                command.tenantId(),
                new ProyectoNombre(command.nombre()),
                new Descripcion(command.descripcion()),
                new Ubicacion(command.ubicacion()),
                command.fechaInicio(),
                command.fechaEstimadaFinalizacion()
        );

        Proyecto savedProyecto = proyectoRepository.save(proyecto);
        
        ProjectCreatedEvent kafkaEvent = new ProjectCreatedEvent(
            savedProyecto.getId().toString(),
            savedProyecto.getTenantId().toString(),
            savedProyecto.getNombre().value(),
            savedProyecto.getUbicacion().value(),
            BigDecimal.ZERO,
            0,
            savedProyecto.getFechaInicio().toLocalDate(),
            savedProyecto.getFechaEstimadaFinalizacion().toLocalDate(),
            "ACTIVO",
            LocalDateTime.now()
        );
        kafkaProducerService.publishProjectCreated(kafkaEvent);
        
        return new ProyectoDto(
                savedProyecto.getId(),
                savedProyecto.getTenantId(),
                savedProyecto.getNombre().value(),
                savedProyecto.getDescripcion() != null ? savedProyecto.getDescripcion().value() : null,
                savedProyecto.getUbicacion().value(),
                savedProyecto.getFechaInicio(),
                savedProyecto.getFechaEstimadaFinalizacion(),
                savedProyecto.isActivo(),
                savedProyecto.getCreatedAt()
        );
    }
}

