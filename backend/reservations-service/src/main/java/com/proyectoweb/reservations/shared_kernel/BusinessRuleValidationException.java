package com.proyectoweb.reservations.shared_kernel;

public class BusinessRuleValidationException extends RuntimeException {
    public BusinessRuleValidationException(BusinessRule brokenRule) {
        super(brokenRule.getMessage());
    }

    public BusinessRuleValidationException(String message) {
        super(message);
    }
}
