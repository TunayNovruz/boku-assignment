package com.boku.assignment.service;

import com.boku.assignment.dto.response.ProviderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class ProviderService {

    @Value("${provider.password}")
    private String password;

    @Value("${provider.user}")
    private String user;

    @Value("${provider.url}")
    private String url;

    public ResponseEntity<Void> sendResponse(ProviderResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((user + ":" +password).getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, authHeaderValue);
        HttpEntity<Object> requestEntity = new HttpEntity<>(response, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Void.class);
    }
}
