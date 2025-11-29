package com.proyectoweb.reservations.shared_kernel;

public interface BusinessRule {
    boolean isBroken();
    String getMessage();
}
