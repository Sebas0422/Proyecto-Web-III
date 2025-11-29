package com.proyectoweb.proyectos.application.queries.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetLotesByProyectoQueryHandler implements Command.Handler<GetLotesByProyectoQuery, List<LoteDto>> {
    
    private final LoteRepository loteRepository;

    public GetLotesByProyectoQueryHandler(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public List<LoteDto> handle(GetLotesByProyectoQuery query) {
        List<Lote> lotes;
        
        if (query.estado() != null) {
            EstadoLote estadoLote = EstadoLote.valueOf(query.estado().toUpperCase());
            lotes = loteRepository.findByProyectoIdAndEstado(query.proyectoId(), estadoLote);
        } else {
            lotes = loteRepository.findByProyectoId(query.proyectoId());
        }

        return lotes.stream()
                .map(l -> new LoteDto(
                        l.getId(),
                        l.getProyectoId(),
                        l.getNumeroLote(),
                        l.getManzana(),
                        l.getGeometria().toWKT(),
                        l.getAreaCalculada(),
                        l.getCentroide().toText(),
                        l.getPrecio().value(),
                        l.getEstado().name(),
                        l.getObservaciones(),
                        l.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
