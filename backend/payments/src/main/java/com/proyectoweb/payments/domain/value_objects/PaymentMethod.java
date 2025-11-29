package com.proyectoweb.payments.domain.value_objects;

public enum PaymentMethod {
    QR,              // Pago mediante código QR
    BANK_TRANSFER,   // Transferencia bancaria
    CASH,            // Efectivo
    CREDIT_CARD,     // Tarjeta de crédito
    DEBIT_CARD       // Tarjeta de débito
}
