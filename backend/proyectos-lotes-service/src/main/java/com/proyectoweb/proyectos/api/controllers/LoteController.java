package com.proyectoweb.proyectos.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.proyectos.api.dto.requests.CreateLoteRequest;
import com.proyectoweb.proyectos.api.dto.responses.ApiResponse;
import com.proyectoweb.proyectos.application.commands.lote.CreateLoteCommand;
import com.proyectoweb.proyectos.application.commands.lote.ImportLotesFromKMLCommand;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.application.queries.lote.GetLotesByProyectoQuery;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final Pipeline pipeline;

    public LoteController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoteDto>> createLote(
            @Valid @RequestBody CreateLoteRequest request,
            @RequestParam UUID proyectoId) {
        
        CreateLoteCommand command = new CreateLoteCommand(
                proyectoId,
                request.getNumeroLote(),
                request.getManzana(),
                request.getGeometriaWKT(),
                request.getPrecio(),
                request.getObservaciones()
        );
        
        LoteDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lote creado exitosamente", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoteDto>>> getLotesByProyecto(
            @RequestParam UUID proyectoId,
            @RequestParam(required = false) String estado) {
        
        GetLotesByProyectoQuery query = new GetLotesByProyectoQuery(proyectoId, estado);
        List<LoteDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/import-kml")
    public ResponseEntity<ApiResponse<List<LoteDto>>> importLotesFromKML(
            @RequestParam UUID proyectoId,
            @RequestParam(required = false) String manzana,
            @RequestParam BigDecimal precio,
            @RequestParam MultipartFile file) {
        
        try {
            ImportLotesFromKMLCommand command = new ImportLotesFromKMLCommand(
                    proyectoId,
                    file,
                    manzana,
                    precio
            );
            
            List<LoteDto> result = command.execute(pipeline);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Lotes importados exitosamente desde KML", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al importar archivo KML: " + e.getMessage(), "KML_IMPORT_ERROR"));
        }
    }
}
