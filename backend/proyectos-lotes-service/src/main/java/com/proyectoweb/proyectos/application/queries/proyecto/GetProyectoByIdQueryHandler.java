package com.proyectoweb.proyectos.application.queries.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import org.springframework.stereotype.Component;

@Component
public class GetProyectoByIdQueryHandler implements Command.Handler<GetProyectoByIdQuery, ProyectoDto> {

    private final ProyectoRepository proyectoRepository;

    public GetProyectoByIdQueryHandler(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public ProyectoDto handle(GetProyectoByIdQuery query) {
        Proyecto proyecto = proyectoRepository.findById(query.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(query.getTenantId())) {
            throw new RuntimeException("No tiene permisos para acceder a este proyecto");
        }

        return new ProyectoDto(
                proyecto.getId(),
                proyecto.getTenantId(),
                proyecto.getNombre().value(),
                proyecto.getDescripcion() != null ? proyecto.getDescripcion().value() : null,
                proyecto.getUbicacion().value(),
                proyecto.getFechaInicio(),
                proyecto.getFechaEstimadaFinalizacion(),
                proyecto.isActivo(),
                proyecto.getCreatedAt()
        );
    }
}
