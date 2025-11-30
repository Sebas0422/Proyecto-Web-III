package com.proyectoweb.payments.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmPaymentRequest {
    
    @NotBlank(message = "Transaction reference is required")
    private String transactionReference;
}
