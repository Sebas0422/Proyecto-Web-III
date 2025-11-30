package com.proyectoweb.payments.domain.value_objects;

public enum PaymentStatus {
    PENDING,      // Pago pendiente
    PROCESSING,   // En proceso de pago
    COMPLETED,    // Pago completado
    FAILED,       // Pago fallido
    REFUNDED,     // Pago reembolsado
    CANCELLED     // Pago cancelado
}
