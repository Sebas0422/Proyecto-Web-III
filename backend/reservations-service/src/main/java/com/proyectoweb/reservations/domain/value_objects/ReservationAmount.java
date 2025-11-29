package com.proyectoweb.reservations.domain.value_objects;

import java.math.BigDecimal;

public record ReservationAmount(BigDecimal amount) {
    public ReservationAmount {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Reservation amount must be greater than zero");
        }
    }
}
