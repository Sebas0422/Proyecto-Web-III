package com.proyectoweb.auth.shared_kernel.core;

public abstract class ValueObject {
    protected void checkRule(BusinessRule rule) throws BusinessRuleValidationException {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }

        if (!rule.isValid()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}
