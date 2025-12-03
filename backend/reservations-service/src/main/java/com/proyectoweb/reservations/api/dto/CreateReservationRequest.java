package com.proyectoweb.reservations.api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateReservationRequest {
    
    @NotNull(message = "Lot ID is required")
    private UUID lotId;
    
    @NotNull(message = "Project ID is required")
    private UUID projectId;
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 200, message = "Customer name must not exceed 200 characters")
    private String customerName;
    
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String customerEmail;
    
    @NotBlank(message = "Customer phone is required")
    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String customerPhone;
    
    @Size(max = 50, message = "Document must not exceed 50 characters")
    private String customerDocument;
    
    @NotNull(message = "Reservation amount is required")
    @DecimalMin(value = "0.01", message = "Reservation amount must be greater than zero")
    private BigDecimal reservationAmount;
    
    @Min(value = 1, message = "Expiration days must be at least 1")
    @Max(value = 90, message = "Expiration days must not exceed 90")
    private int expirationDays = 7;
    
    private String notes;
}
