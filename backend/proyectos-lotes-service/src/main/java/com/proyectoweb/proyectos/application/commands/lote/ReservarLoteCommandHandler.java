package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReservarLoteCommandHandler implements Command.Handler<ReservarLoteCommand, Void> {
    
    private static final Logger logger = LoggerFactory.getLogger(ReservarLoteCommandHandler.class);
    
    private final LoteRepository loteRepository;

    public ReservarLoteCommandHandler(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public Void handle(ReservarLoteCommand command) {
        logger.info("Ejecutando comando para reservar lote: {}", command.loteId());
        
        Lote lote = loteRepository.findById(command.loteId())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado con ID: " + command.loteId()));
        
        lote.reservar();
        loteRepository.save(lote);
        
        logger.info("Lote {} reservado exitosamente", command.loteId());
        return null;
    }
}
