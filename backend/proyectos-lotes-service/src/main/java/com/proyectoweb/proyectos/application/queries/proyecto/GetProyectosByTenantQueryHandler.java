package com.proyectoweb.proyectos.application.queries.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetProyectosByTenantQueryHandler implements Command.Handler<GetProyectosByTenantQuery, List<ProyectoDto>> {
    
    private final ProyectoRepository proyectoRepository;

    public GetProyectosByTenantQueryHandler(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public List<ProyectoDto> handle(GetProyectosByTenantQuery query) {
        List<Proyecto> proyectos;
        
        if (query.soloActivos() != null && query.soloActivos()) {
            proyectos = proyectoRepository.findByTenantIdAndActivo(query.tenantId(), true);
        } else {
            proyectos = proyectoRepository.findByTenantId(query.tenantId());
        }

        return proyectos.stream()
                .map(p -> new ProyectoDto(
                        p.getId(),
                        p.getTenantId(),
                        p.getNombre().value(),
                        p.getDescripcion() != null ? p.getDescripcion().value() : null,
                        p.getUbicacion().value(),
                        p.getFechaInicio(),
                        p.getFechaEstimadaFinalizacion(),
                        p.isActivo(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
