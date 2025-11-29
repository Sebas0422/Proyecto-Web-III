package com.proyectoweb.reservations.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reservations.api.dto.ApiResponse;
import com.proyectoweb.reservations.api.dto.CreateLeadRequest;
import com.proyectoweb.reservations.application.commands.lead.CreateLeadCommand;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.application.queries.lead.GetLeadsByTenantQuery;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final Pipeline pipeline;

    @PostMapping
    public ResponseEntity<ApiResponse<LeadDto>> createLead(
            @Valid @RequestBody CreateLeadRequest request,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        CreateLeadCommand command = new CreateLeadCommand(
                tenantId,
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerPhone(),
                request.getCustomerDocument(),
                request.getSource(),
                request.getInterestLevel(),
                request.getNotes(),
                request.getProjectId(),
                request.getLotId()
        );

        LeadDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead created successfully", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeadDto>>> getLeads(
            @RequestParam(required = false) String status,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GetLeadsByTenantQuery query = new GetLeadsByTenantQuery(tenantId, status);
        List<LeadDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
