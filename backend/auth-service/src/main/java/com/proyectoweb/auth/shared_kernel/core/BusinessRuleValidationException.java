package com.proyectoweb.auth.shared_kernel.core;

import lombok.Getter;

@Getter
public class BusinessRuleValidationException extends RuntimeException {
    private final String message;
    private final String ruleType;

    public BusinessRuleValidationException(BusinessRule rule) {
        super(rule.getMessage());
        this.message = rule.getMessage();
        this.ruleType = rule.getClass().getSimpleName();
    }

    public BusinessRuleValidationException(String message) {
        super(message);
        this.message = message;
        this.ruleType = "GenericValidation";
    }
}
