package com.proyectoweb.proyectos.infrastructure.messaging;

import com.proyectoweb.proyectos.infrastructure.messaging.events.LotCreatedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LotStatusChangedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String PROJECT_EVENTS_TOPIC = "project-events";
    private static final String LOT_EVENTS_TOPIC = "lot-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProjectCreated(ProjectCreatedEvent event) {
        kafkaTemplate.send(PROJECT_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLotCreated(LotCreatedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLotStatusChanged(LotStatusChangedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }
}
