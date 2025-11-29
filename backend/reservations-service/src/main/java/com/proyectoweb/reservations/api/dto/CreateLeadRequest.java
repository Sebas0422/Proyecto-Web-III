package com.proyectoweb.reservations.api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateLeadRequest {
    
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
    
    @NotBlank(message = "Source is required")
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;
    
    @Size(max = 20, message = "Interest level must not exceed 20 characters")
    private String interestLevel = "MEDIUM"; // HIGH, MEDIUM, LOW
    
    private String notes;
    
    private UUID projectId; // Optional
    
    private UUID lotId; // Optional
}
