package com.proyectoweb.proyectos.application.queries.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import org.springframework.stereotype.Component;

@Component
public class GetLoteByIdQueryHandler implements Command.Handler<GetLoteByIdQuery, LoteDto> {

    private final LoteRepository loteRepository;
    private final ProyectoRepository proyectoRepository;

    public GetLoteByIdQueryHandler(LoteRepository loteRepository, ProyectoRepository proyectoRepository) {
        this.loteRepository = loteRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public LoteDto handle(GetLoteByIdQuery query) {
        Lote lote = loteRepository.findById(query.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Validar que el proyecto pertenece al tenant
        Proyecto proyecto = proyectoRepository.findById(lote.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getTenantId().equals(query.getTenantId())) {
            throw new RuntimeException("No tiene permisos para acceder a este lote");
        }

        return new LoteDto(
                lote.getId(),
                lote.getProyectoId(),
                lote.getNumeroLote(),
                lote.getManzana(),
                lote.getGeometria().toWKT(),
                lote.getAreaCalculada(),
                lote.getCentroide().toText(),
                lote.getPrecio().value(),
                lote.getEstado().name(),
                lote.getObservaciones(),
                lote.getCreatedAt()
        );
    }
}
