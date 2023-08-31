package com.boku.assignment.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MerchantRequest {
    private String shortcode;
    private String keyword;
    private String message;
    private String operator;
    private String sender;
    private String transaction_id;

    public MerchantRequest(NotificationDto notification) {
        shortcode = notification.getReceiver();
        keyword = notification.getKeyword();
        operator = notification.getOperator();
        sender = notification.getSender();
        transaction_id = UUID.randomUUID().toString();
    }
}
