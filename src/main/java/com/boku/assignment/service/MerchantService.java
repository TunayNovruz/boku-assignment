package com.boku.assignment.service;

import com.boku.assignment.dto.request.MerchantRequest;
import com.boku.assignment.dto.request.NotificationDto;
import com.boku.assignment.dto.response.MerchantResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MerchantService {
    public  ResponseEntity<MerchantResponse> sendNotification(NotificationDto notification) {
        String merchantUrl = determineMerchantUrl(notification.getKeyword());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MerchantRequest request = new MerchantRequest(notification);
        HttpEntity<MerchantRequest> requestEntity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                merchantUrl, HttpMethod.POST, requestEntity, MerchantResponse.class);
    }

    private String determineMerchantUrl(String keyword) {
        //todo
        return "https://testmerchant.fortumo.mobi/api/sms/" + keyword.toLowerCase();
    }
}
