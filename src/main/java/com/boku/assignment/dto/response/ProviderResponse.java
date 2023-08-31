package com.boku.assignment.dto.response;

import com.boku.assignment.dto.request.NotificationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProviderResponse {
    private String message;
    private String mo_message_id;
    private String receiver;
    private String operator;

    public ProviderResponse(NotificationDto notification,String replyMessage){
        message = replyMessage;
        mo_message_id = notification.getMessage_id();
        receiver = notification.getReceiver();
        operator = notification.getOperator();
    }
}
