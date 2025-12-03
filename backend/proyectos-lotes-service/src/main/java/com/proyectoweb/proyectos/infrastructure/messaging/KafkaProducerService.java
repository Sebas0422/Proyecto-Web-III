package com.proyectoweb.proyectos.infrastructure.messaging;

import com.proyectoweb.proyectos.infrastructure.messaging.events.LotCreatedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LotStatusChangedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LoteUpdatedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.LoteDeletedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectCreatedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectUpdatedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ProjectDeletedEvent;
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
        kafkaTemplate.send(PROJECT_EVENTS_TOPIC, event.getTenantId(), event);
    }

    public void publishProjectUpdated(ProjectUpdatedEvent event) {
        kafkaTemplate.send(PROJECT_EVENTS_TOPIC, event.getTenantId(), event);
    }

    public void publishProjectDeleted(ProjectDeletedEvent event) {
        kafkaTemplate.send(PROJECT_EVENTS_TOPIC, event.getTenantId(), event);
    }

    public void publishLotCreated(LotCreatedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLotStatusChanged(LotStatusChangedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLoteUpdated(LoteUpdatedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId(), event);
    }

    public void publishLoteDeleted(LoteDeletedEvent event) {
        kafkaTemplate.send(LOT_EVENTS_TOPIC, event.getTenantId(), event);
    }
}
