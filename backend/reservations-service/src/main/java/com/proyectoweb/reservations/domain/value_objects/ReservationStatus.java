package com.proyectoweb.reservations.domain.value_objects;

public enum ReservationStatus {
    PENDING,    // Reserva creada, esperando confirmación/pago
    CONFIRMED,  // Reserva confirmada con pago inicial
    EXPIRED,    // Reserva expirada por falta de pago
    CANCELLED   // Reserva cancelada manualmente
}
