package com.proyectoweb.payments.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.payments.api.dto.ApiResponse;
import com.proyectoweb.payments.application.commands.receipt.GenerateReceiptCommand;
import com.proyectoweb.payments.application.dto.ReceiptDto;
import com.proyectoweb.payments.application.queries.receipt.GetReceiptByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final Pipeline pipeline;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ReceiptDto>> generateReceipt(
            @RequestParam UUID paymentId,
            Authentication authentication) {
        
        UUID userId = (UUID) authentication.getPrincipal();
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GenerateReceiptCommand command = new GenerateReceiptCommand(
                paymentId,
                tenantId,
                userId
        );

        ReceiptDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Receipt generated successfully", result));
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ApiResponse<ReceiptDto>> getReceipt(
            @PathVariable UUID receiptId,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GetReceiptByIdQuery query = new GetReceiptByIdQuery(receiptId, tenantId);
        ReceiptDto result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
