package com.proyectoweb.payments.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.payments.api.dto.ApiResponse;
import com.proyectoweb.payments.api.dto.ConfirmPaymentRequest;
import com.proyectoweb.payments.api.dto.CreatePaymentRequest;
import com.proyectoweb.payments.application.commands.payment.ConfirmPaymentCommand;
import com.proyectoweb.payments.application.commands.payment.CreatePaymentCommand;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.application.queries.payment.GetPaymentsByTenantQuery;
import com.proyectoweb.payments.application.services.QRCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final Pipeline pipeline;
    private final QRCodeService qrCodeService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDto>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            Authentication authentication) {
        
        UUID userId = (UUID) authentication.getPrincipal();
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        CreatePaymentCommand command = new CreatePaymentCommand(
                tenantId,
                request.getReservationId(),
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerPhone(),
                request.getCustomerDocument(),
                request.getAmount(),
                request.getCurrency(),
                request.getPaymentMethod(),
                request.getExpirationHours(),
                userId,
                request.getNotes()
        );

        PaymentDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created successfully", result));
    }

    @PutMapping("/{paymentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentDto>> confirmPayment(
            @PathVariable UUID paymentId,
            @Valid @RequestBody ConfirmPaymentRequest request,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        ConfirmPaymentCommand command = new ConfirmPaymentCommand(
                paymentId,
                tenantId,
                request.getTransactionReference()
        );
        
        PaymentDto result = command.execute(pipeline);
        return ResponseEntity.ok(ApiResponse.success("Payment confirmed successfully", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getPayments(
            @RequestParam(required = false) String status,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GetPaymentsByTenantQuery query = new GetPaymentsByTenantQuery(tenantId, status);
        List<PaymentDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{paymentId}/qr")
    public ResponseEntity<byte[]> getPaymentQRCode(
            @PathVariable UUID paymentId,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Get payment by ID (simplified - should use query)
        GetPaymentsByTenantQuery query = new GetPaymentsByTenantQuery(tenantId, null);
        List<PaymentDto> payments = query.execute(pipeline);
        
        PaymentDto payment = payments.stream()
                .filter(p -> p.id().equals(paymentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        byte[] qrCode = qrCodeService.generateQRCodeBytes(payment.qrCodeData());
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
}
