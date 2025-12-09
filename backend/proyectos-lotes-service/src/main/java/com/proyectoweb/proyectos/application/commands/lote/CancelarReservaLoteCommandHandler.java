package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CancelarReservaLoteCommandHandler implements Command.Handler<CancelarReservaLoteCommand, Void> {
    
    private static final Logger logger = LoggerFactory.getLogger(CancelarReservaLoteCommandHandler.class);
    
    private final LoteRepository loteRepository;

    public CancelarReservaLoteCommandHandler(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public Void handle(CancelarReservaLoteCommand command) {
        logger.info("Ejecutando comando para cancelar reserva de lote: {}", command.loteId());
        
        Lote lote = loteRepository.findById(command.loteId())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado con ID: " + command.loteId()));
        
        lote.cancelarReserva();
        loteRepository.save(lote);
        
        logger.info("Reserva cancelada, lote {} marcado como DISPONIBLE", command.loteId());
        return null;
    }
}
