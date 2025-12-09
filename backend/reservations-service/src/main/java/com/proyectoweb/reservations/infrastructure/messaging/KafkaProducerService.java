package com.proyectoweb.reservations.infrastructure.messaging;

import com.proyectoweb.reservations.infrastructure.messaging.events.LeadConvertedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadCreatedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadDeletedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadStatusChangedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationCancelledEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationConfirmedEvent;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String RESERVATION_CREATED_TOPIC = "reservation-created-events";
    private static final String RESERVATION_CONFIRMED_TOPIC = "reservation-confirmed-events";
    private static final String RESERVATION_CANCELLED_TOPIC = "reservation-cancelled-events";
    private static final String PAYMENT_COMPLETED_TOPIC = "reservation-payment-completed-events";
    private static final String LEAD_EVENTS_TOPIC = "lead-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void publishReservationCreated(ReservationCreatedEvent event) {
        kafkaTemplate.send(RESERVATION_CREATED_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishReservationConfirmed(ReservationConfirmedEvent event) {
        kafkaTemplate.send(RESERVATION_CONFIRMED_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishReservationCancelled(ReservationCancelledEvent event) {
        kafkaTemplate.send(RESERVATION_CANCELLED_TOPIC, event.getTenantId(), event);
    }

    public void publishPaymentCompleted(com.proyectoweb.reservations.infrastructure.messaging.events.PaymentCompletedEvent event) {
        kafkaTemplate.send(PAYMENT_COMPLETED_TOPIC, event.getTenantId(), event);
    }

    public void publishLeadCreated(LeadCreatedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLeadConverted(LeadConvertedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLeadStatusChanged(LeadStatusChangedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }

    public void publishLeadDeleted(LeadDeletedEvent event) {
        kafkaTemplate.send(LEAD_EVENTS_TOPIC, event.getTenantId().toString(), event);
    }
}
