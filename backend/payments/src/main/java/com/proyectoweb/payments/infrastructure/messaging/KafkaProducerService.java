package com.proyectoweb.payments.infrastructure.messaging;

import com.proyectoweb.payments.infrastructure.messaging.events.PaymentCompletedEvent;
import com.proyectoweb.payments.infrastructure.messaging.events.ReceiptGeneratedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String PAYMENT_COMPLETED_TOPIC = "payment-completed-events";
    private static final String RECEIPT_GENERATED_TOPIC = "receipt-generated-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        kafkaTemplate.send(PAYMENT_COMPLETED_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishReceiptGenerated(ReceiptGeneratedEvent event) {
        kafkaTemplate.send(RECEIPT_GENERATED_TOPIC, event.getTenantId().toString(), event);
    }
}
