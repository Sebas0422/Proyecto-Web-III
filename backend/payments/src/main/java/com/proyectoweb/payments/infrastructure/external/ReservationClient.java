package com.proyectoweb.payments.infrastructure.external;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.UUID;

@Component
public class ReservationClient {
    private final RestTemplate restTemplate;
    private final String reservationsServiceUrl;

    public ReservationClient(@Value("${reservations.service.url:http://localhost:8000}") String reservationsServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.reservationsServiceUrl = reservationsServiceUrl;
    }

    public ReservationResponse getReservationById(UUID reservationId, String token) {
        String url = reservationsServiceUrl + "/api/reservations/" + reservationId;
        HttpHeaders headers = new HttpHeaders();
        String bearerToken = (token != null && token.startsWith("Bearer ")) ? token : ("Bearer " + token);
        headers.set("Authorization", bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ReservationApiResponse> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            ReservationApiResponse.class
        );
        ReservationApiResponse apiResponse = response.getBody();
        return (apiResponse != null) ? apiResponse.data : null;
    }

    public static class ReservationResponse {
        public UUID id;
        public UUID tenantId;
        public String lotId;
        public UUID projectId;
        public String customerName;
        public String customerEmail;
        public String customerPhone;
        public String customerDocument;
        public String status;
    }

    public static class ReservationApiResponse {
        public boolean success;
        public String message;
        public ReservationResponse data;
        public String errorCode;
    }
}
