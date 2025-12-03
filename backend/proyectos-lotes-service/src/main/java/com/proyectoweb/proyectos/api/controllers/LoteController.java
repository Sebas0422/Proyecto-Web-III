package com.proyectoweb.proyectos.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.proyectos.api.dto.requests.CreateLoteRequest;
import com.proyectoweb.proyectos.api.dto.requests.UpdateLoteRequest;
import com.proyectoweb.proyectos.api.dto.responses.ApiResponse;
import com.proyectoweb.proyectos.application.commands.lote.CreateLoteCommand;
import com.proyectoweb.proyectos.application.commands.lote.UpdateLoteCommand;
import com.proyectoweb.proyectos.application.commands.lote.DeleteLoteCommand;
import com.proyectoweb.proyectos.application.commands.lote.ImportLotesFromKMLCommand;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import com.proyectoweb.proyectos.application.queries.lote.GetLotesByProyectoQuery;
import com.proyectoweb.proyectos.application.queries.lote.GetLoteByIdQuery;
import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final Pipeline pipeline;
    private final ProyectoRepository proyectoRepository;

    public LoteController(Pipeline pipeline, ProyectoRepository proyectoRepository) {
        this.pipeline = pipeline;
        this.proyectoRepository = proyectoRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoteDto>> createLote(
            @Valid @RequestBody CreateLoteRequest request,
            @RequestParam UUID proyectoId,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        // Validar que el proyecto pertenece al tenant
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        if (!proyecto.getTenantId().equals(tenantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("No tiene permisos para crear lotes en este proyecto", "FORBIDDEN"));
        }
        
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
            @RequestParam(required = false) String estado,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        // Validar que el proyecto pertenece al tenant
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        if (!proyecto.getTenantId().equals(tenantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("No tiene permisos para ver lotes de este proyecto", "FORBIDDEN"));
        }
        
        GetLotesByProyectoQuery query = new GetLotesByProyectoQuery(proyectoId, estado);
        List<LoteDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/import-kml")
    public ResponseEntity<ApiResponse<List<LoteDto>>> importLotesFromKML(
            @RequestParam UUID proyectoId,
            @RequestParam(required = false) String manzana,
            @RequestParam BigDecimal precio,
            @RequestParam MultipartFile file,
            Authentication authentication) {
        
        try {
            UUID tenantId = UUID.fromString((String) authentication.getDetails());
            
            // Validar que el proyecto pertenece al tenant
            Proyecto proyecto = proyectoRepository.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            
            if (!proyecto.getTenantId().equals(tenantId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("No tiene permisos para importar lotes en este proyecto", "FORBIDDEN"));
            }
            
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoteDto>> getLoteById(
            @PathVariable UUID id,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        GetLoteByIdQuery query = new GetLoteByIdQuery(id, tenantId);
        LoteDto result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoteDto>> updateLote(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLoteRequest request,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        UpdateLoteCommand command = new UpdateLoteCommand(
                id,
                tenantId,
                request.getPrecio(),
                request.getEstado(),
                request.getObservaciones()
        );
        
        LoteDto result = command.execute(pipeline);
        return ResponseEntity.ok(ApiResponse.success("Lote actualizado exitosamente", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLote(
            @PathVariable UUID id,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        DeleteLoteCommand command = new DeleteLoteCommand(id, tenantId);
        command.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success("Lote eliminado exitosamente (soft delete)", null));
    }
}
