package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateLoteCommandHandler implements Command.Handler<CreateLoteCommand, LoteDto> {
    
    private final LoteRepository loteRepository;

    public CreateLoteCommandHandler(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public LoteDto handle(CreateLoteCommand command) {
        if (loteRepository.existsByProyectoIdAndNumeroLote(command.proyectoId(), command.numeroLote())) {
            throw new IllegalArgumentException("Ya existe un lote con ese número en el proyecto");
        }

        LoteGeometria geometria = LoteGeometria.fromWKT(command.geometriaWKT());
        
        Lote lote = Lote.crear(
                UUID.randomUUID(),
                command.proyectoId(),
                command.numeroLote(),
                command.manzana(),
                geometria,
                new Precio(command.precio()),
                command.observaciones()
        );

        Lote savedLote = loteRepository.save(lote);
        
        return new LoteDto(
                savedLote.getId(),
                savedLote.getProyectoId(),
                savedLote.getNumeroLote(),
                savedLote.getManzana(),
                savedLote.getGeometria().toWKT(),
                savedLote.getAreaCalculada(),
                savedLote.getCentroide() != null ? "POINT(" + savedLote.getCentroide().getX() + " " + savedLote.getCentroide().getY() + ")" : null,
                savedLote.getPrecio().value(),
                savedLote.getEstado().name(),
                savedLote.getObservaciones(),
                savedLote.getCreatedAt()
        );
    }
}
