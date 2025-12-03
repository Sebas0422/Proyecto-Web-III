package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import com.proyectoweb.proyectos.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectUpdatedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class UpdateProyectoCommandHandler implements Command.Handler<UpdateProyectoCommand, ProyectoDto> {

    private final ProyectoRepository proyectoRepository;
    private final KafkaProducerService kafkaProducerService;

    public UpdateProyectoCommandHandler(ProyectoRepository proyectoRepository, KafkaProducerService kafkaProducerService) {
        this.proyectoRepository = proyectoRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public ProyectoDto handle(UpdateProyectoCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No tiene permisos para actualizar este proyecto");
        }

        proyecto.updateNombre(new ProyectoNombre(command.getNombre()));
        proyecto.updateUbicacion(new Ubicacion(command.getUbicacion()));
        
        if (command.getDescripcion() != null && !command.getDescripcion().isEmpty()) {
            proyecto.updateDescripcion(new Descripcion(command.getDescripcion()));
        }
        
        if (command.getFechaInicio() != null) {
            proyecto.updateFechaInicio(command.getFechaInicio());
        }
        
        if (command.getFechaEstimadaFinalizacion() != null) {
            proyecto.updateFechaEstimadaFinalizacion(command.getFechaEstimadaFinalizacion());
        }

        Proyecto updatedProyecto = proyectoRepository.save(proyecto);

        ProjectUpdatedEvent kafkaEvent = new ProjectUpdatedEvent(
            updatedProyecto.getId().toString(),
            updatedProyecto.getTenantId().toString(),
            updatedProyecto.getNombre().value(),
            updatedProyecto.getUbicacion().value(),
            BigDecimal.ZERO,
            0,
            updatedProyecto.getFechaInicio().toLocalDate(),
            updatedProyecto.getFechaEstimadaFinalizacion().toLocalDate(),
            updatedProyecto.isActivo() ? "ACTIVO" : "INACTIVO",
            LocalDateTime.now()
        );
        kafkaProducerService.publishProjectUpdated(kafkaEvent);

        return new ProyectoDto(
                updatedProyecto.getId(),
                updatedProyecto.getTenantId(),
                updatedProyecto.getNombre().value(),
                updatedProyecto.getDescripcion() != null ? updatedProyecto.getDescripcion().value() : null,
                updatedProyecto.getUbicacion().value(),
                updatedProyecto.getFechaInicio(),
                updatedProyecto.getFechaEstimadaFinalizacion(),
                updatedProyecto.isActivo(),
                updatedProyecto.getCreatedAt()
        );
    }
}
