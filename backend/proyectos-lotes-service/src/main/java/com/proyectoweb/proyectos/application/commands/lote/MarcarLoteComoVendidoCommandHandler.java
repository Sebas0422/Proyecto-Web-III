package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MarcarLoteComoVendidoCommandHandler implements Command.Handler<MarcarLoteComoVendidoCommand, Void> {
    
    private static final Logger logger = LoggerFactory.getLogger(MarcarLoteComoVendidoCommandHandler.class);
    
    private final LoteRepository loteRepository;

    public MarcarLoteComoVendidoCommandHandler(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public Void handle(MarcarLoteComoVendidoCommand command) {
        logger.info("Ejecutando comando para marcar lote como vendido: {}", command.loteId());
        
        Lote lote = loteRepository.findById(command.loteId())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado con ID: " + command.loteId()));
        
        lote.marcarComoVendido();
        loteRepository.save(lote);
        
        logger.info("Lote {} marcado como VENDIDO exitosamente", command.loteId());
        return null;
    }
}
