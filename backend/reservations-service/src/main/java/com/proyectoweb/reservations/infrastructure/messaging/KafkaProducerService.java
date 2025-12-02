package com.proyectoweb.reservations.infrastructure.messaging;

import com.proyectoweb.reservations.infrastructure.messaging.events.LeadConvertedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadCreatedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationConfirmedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String RESERVATION_EVENTS_TOPIC = "reservation-events";
    private static final String LEAD_EVENTS_TOPIC = "lead-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReservationCreated(ReservationCreatedEvent event) {
        kafkaTemplate.send(RESERVATION_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishReservationConfirmed(ReservationConfirmedEvent event) {
        kafkaTemplate.send(RESERVATION_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLeadCreated(LeadCreatedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLeadConverted(LeadConvertedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }
}
