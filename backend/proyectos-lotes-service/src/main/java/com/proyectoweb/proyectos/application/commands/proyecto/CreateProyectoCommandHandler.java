package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateProyectoCommandHandler implements Command.Handler<CreateProyectoCommand, ProyectoDto> {
    
    private final ProyectoRepository proyectoRepository;

    public CreateProyectoCommandHandler(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
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
