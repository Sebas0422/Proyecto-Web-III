package com.proyectoweb.reservations.domain.value_objects;

public enum LeadStatus {
    NEW,         // Lead recién creado
    CONTACTED,   // Ya se contactó al cliente
    INTERESTED,  // Cliente muestra interés
    NEGOTIATING, // En proceso de negociación
    CONVERTED,   // Convertido a reserva/venta
    LOST         // Lead perdido
}
