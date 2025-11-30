package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.services.KmlParserService;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImportLotesFromKMLCommandHandler implements Command.Handler<ImportLotesFromKMLCommand, List<LoteDto>> {
    
    private final LoteRepository loteRepository;
    private final KmlParserService kmlParserService;

    public ImportLotesFromKMLCommandHandler(
            LoteRepository loteRepository,
            KmlParserService kmlParserService) {
        this.loteRepository = loteRepository;
        this.kmlParserService = kmlParserService;
    }

    @Override
    public List<LoteDto> handle(ImportLotesFromKMLCommand command) {
        try {
            List<KmlParserService.LoteData> lotesData = kmlParserService.parseKml(
                    command.kmlFile().getInputStream()
            );

            List<LoteDto> lotesCreados = new ArrayList<>();
            
            for (KmlParserService.LoteData loteData : lotesData) {
                String numeroLote = loteData.nombre() != null ? loteData.nombre() : "LOTE_" + UUID.randomUUID().toString().substring(0, 8);
                
                if (!loteRepository.existsByProyectoIdAndNumeroLote(command.proyectoId(), numeroLote)) {
                    Lote lote = Lote.crear(
                            UUID.randomUUID(),
                            command.proyectoId(),
                            numeroLote,
                            command.manzanaDefault(),
                            loteData.geometria(),
                            new Precio(command.precioDefault()),
                            loteData.descripcion()
                    );
                    
                    Lote savedLote = loteRepository.save(lote);
                    
                    LoteDto loteDto = new LoteDto(
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
                    
                    lotesCreados.add(loteDto);
                }
            }
            
            return lotesCreados;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al importar lotes desde KML: " + e.getMessage(), e);
        }
    }
}
