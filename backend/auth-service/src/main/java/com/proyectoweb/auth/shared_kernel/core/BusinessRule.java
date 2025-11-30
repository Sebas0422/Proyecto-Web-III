package com.proyectoweb.auth.shared_kernel.core;

public interface BusinessRule {
    boolean isValid();
    String getMessage();
}
