package com.proyectoweb.proyectos.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.proyectos.api.dto.responses.ApiResponse;
import com.proyectoweb.proyectos.application.commands.attachment.DeleteAttachmentCommand;
import com.proyectoweb.proyectos.application.commands.attachment.UploadAttachmentCommand;
import com.proyectoweb.proyectos.application.dto.LoteAttachmentDto;
import com.proyectoweb.proyectos.application.queries.attachment.GetAttachmentsByLoteQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lotes/{loteId}/attachments")
public class LoteAttachmentController {

    private final Pipeline pipeline;

    public LoteAttachmentController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoteAttachmentDto>> uploadAttachment(
            @PathVariable UUID loteId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());
        String userId = (String) authentication.getPrincipal();

        UploadAttachmentCommand command = new UploadAttachmentCommand(loteId, tenantId, file, userId);
        LoteAttachmentDto result = command.execute(pipeline);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Archivo subido exitosamente", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoteAttachmentDto>>> getAttachments(
            @PathVariable UUID loteId,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());

        GetAttachmentsByLoteQuery query = new GetAttachmentsByLoteQuery(loteId, tenantId);
        List<LoteAttachmentDto> result = query.execute(pipeline);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(
            @PathVariable UUID loteId,
            @PathVariable UUID attachmentId,
            Authentication authentication) {
        
        UUID tenantId = UUID.fromString((String) authentication.getDetails());

        DeleteAttachmentCommand command = new DeleteAttachmentCommand(attachmentId, tenantId);
        command.execute(pipeline);

        return ResponseEntity.ok(ApiResponse.success("Archivo eliminado exitosamente", null));
    }
}
