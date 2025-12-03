package com.proyectoweb.reservations.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateLeadStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}
