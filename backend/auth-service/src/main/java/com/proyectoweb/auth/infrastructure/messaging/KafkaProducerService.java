package com.proyectoweb.auth.infrastructure.messaging;

import com.proyectoweb.auth.infrastructure.messaging.events.CompanyCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String COMPANY_EVENTS_TOPIC = "company-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCompanyCreated(CompanyCreatedEvent event) {
        kafkaTemplate.send(COMPANY_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }
}
