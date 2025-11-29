package com.proyectoweb.reservations.domain.value_objects;

public record CustomerInfo(
        String fullName,
        String email,
        String phone,
        String documentNumber
) {
    public CustomerInfo {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
    }
}
