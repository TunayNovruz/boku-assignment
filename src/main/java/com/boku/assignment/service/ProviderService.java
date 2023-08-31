package com.boku.assignment.service;

import com.boku.assignment.dto.response.ProviderResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class ProviderService {
    public ResponseEntity<Void> sendResponse(ProviderResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString("fortumo:topsecret".getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, authHeaderValue);
        HttpEntity<Object> requestEntity = new HttpEntity<>(response, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://testprovider.fortumo.mobi/sms/send",
                HttpMethod.GET,
                requestEntity,
                Void.class);
    }
}
