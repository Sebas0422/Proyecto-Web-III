package com.proyectoweb.proyectos.shared_kernel;

public class BusinessRuleValidationException extends RuntimeException {
    private final BusinessRule brokenRule;

    public BusinessRuleValidationException(BusinessRule brokenRule) {
        super(brokenRule.getMessage());
        this.brokenRule = brokenRule;
    }

    public BusinessRule getBrokenRule() {
        return brokenRule;
    }

    @Override
    public String toString() {
        return brokenRule.getMessage();
    }
}
