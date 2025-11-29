package com.proyectoweb.payments.api.dto;

import com.proyectoweb.payments.domain.value_objects.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentRequest {
    
    @NotNull(message = "Reservation ID is required")
    private UUID reservationId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Customer phone is required")
    private String customerPhone;

    private String customerDocument;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String currency = "BOB";

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Min(value = 1, message = "Expiration hours must be at least 1")
    private int expirationHours = 24;

    private String notes;
}
