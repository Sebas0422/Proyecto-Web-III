package com.proyectoweb.reservations.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reservations.api.dto.ApiResponse;
import com.proyectoweb.reservations.api.dto.CreateReservationRequest;
import com.proyectoweb.reservations.application.commands.reservation.CancelReservationCommand;
import com.proyectoweb.reservations.application.commands.reservation.ConfirmReservationCommand;
import com.proyectoweb.reservations.application.commands.reservation.CreateReservationCommand;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.application.queries.reservation.GetReservationsByTenantQuery;
import com.proyectoweb.reservations.application.queries.reservation.GetReservationByIdQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final Pipeline pipeline;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDto>> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            Authentication authentication) {
        
        UUID userId = (UUID) authentication.getPrincipal();
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        CreateReservationCommand command = new CreateReservationCommand(
                tenantId,
                request.getLotId(),
                request.getProjectId(),
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerPhone(),
                request.getCustomerDocument(),
                request.getReservationAmount(),
                request.getExpirationDays(),
                userId,
                request.getNotes()
        );

        ReservationDto result = command.execute(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reservation created successfully", result));
    }

    @PutMapping("/{reservationId}/confirm")
    public ResponseEntity<ApiResponse<ReservationDto>> confirmReservation(
            @PathVariable UUID reservationId,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        ConfirmReservationCommand command = new ConfirmReservationCommand(reservationId, tenantId);
        ReservationDto result = command.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success("Reservation confirmed successfully", result));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDto>> cancelReservation(
            @PathVariable UUID reservationId,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        CancelReservationCommand command = new CancelReservationCommand(reservationId, tenantId);
        ReservationDto result = command.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDto>> getReservationById(
            @PathVariable UUID id,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GetReservationByIdQuery query = new GetReservationByIdQuery(id, tenantId);
        ReservationDto result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getReservations(
            @RequestParam(required = false) String status,
            Authentication authentication) {
        
        UUID tenantId = (UUID) authentication.getDetails();

        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Tenant ID not found in token", "NO_TENANT"));
        }

        GetReservationsByTenantQuery query = new GetReservationsByTenantQuery(tenantId, status);
        List<ReservationDto> result = query.execute(pipeline);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
