package com.boku.assignment.dto.request;

import com.boku.assignment.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {
    private String message_id;
    private String sender;
    private String text;
    private String receiver;
    private String operator;
    private String timestamp;

    public String getKeyword(){
        return StringUtils.findFirstWord(text);
    }
}

