package com.proyectoweb.proyectos.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.proyectos.api.dto.requests.CreateProyectoRequest;
import com.proyectoweb.proyectos.api.dto.requests.UpdateProyectoRequest;
import com.proyectoweb.proyectos.api.dto.responses.ApiResponse;
import com.proyectoweb.proyectos.application.commands.proyecto.CreateProyectoCommand;
import com.proyectoweb.proyectos.application.commands.proyecto.UpdateProyectoCommand;
import com.proyectoweb.proyectos.application.commands.proyecto.DeleteProyectoCommand;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;
import com.proyectoweb.proyectos.application.queries.proyecto.GetProyectosByTenantQuery;
import com.proyectoweb.proyectos.application.queries.proyecto.GetProyectoByIdQuery;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final Pipeline pipeline;

    public ProyectoController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProyectoDto>> createProyecto(
            @Valid @RequestBody CreateProyectoRequest request,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        CreateProyectoCommand command = new CreateProyectoCommand(
                tenantId,
                request.getNombre(),
                request.getDescripcion(),
                request.getUbicacion(),
                request.getFechaInicio().atStartOfDay(),
                request.getFechaEstimadaFinalizacion() != null ? request.getFechaEstimadaFinalizacion().atStartOfDay() : null
        );
        
        ProyectoDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proyecto creado exitosamente", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProyectoDto>>> getProyectosByTenant(
            @RequestParam(required = false, defaultValue = "true") Boolean activo,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        GetProyectosByTenantQuery query = new GetProyectosByTenantQuery(tenantId, activo);
        List<ProyectoDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProyectoDto>> getProyectoById(
            @PathVariable UUID id,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        GetProyectoByIdQuery query = new GetProyectoByIdQuery(id, tenantId);
        ProyectoDto result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProyectoDto>> updateProyecto(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProyectoRequest request,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        UpdateProyectoCommand command = new UpdateProyectoCommand(
                id,
                tenantId,
                request.getNombre(),
                request.getDescripcion(),
                request.getUbicacion(),
                request.getFechaInicio().atStartOfDay(),
                request.getFechaEstimadaFinalizacion() != null ? request.getFechaEstimadaFinalizacion().atStartOfDay() : null
        );
        
        ProyectoDto result = command.execute(pipeline);
        return ResponseEntity.ok(ApiResponse.success("Proyecto actualizado exitosamente", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProyecto(
            @PathVariable UUID id,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        
        DeleteProyectoCommand command = new DeleteProyectoCommand(id, tenantId);
        command.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success("Proyecto eliminado exitosamente", null));
    }
}
