package com.boku.assignment.service;

import com.boku.assignment.dto.request.NotificationDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class NotificationService {
    private final ConcurrentLinkedQueue<NotificationDto> notificationQueue = new ConcurrentLinkedQueue<>();

    public void addNotification(NotificationDto notificationDto) {
        System.out.println("registered:" + notificationDto.getKeyword());
        notificationQueue.add(notificationDto);
    }

    public List<NotificationDto> getNextNotifications() {

        List<NotificationDto> notificationsBatch = new ArrayList<>();
        while (!notificationQueue.isEmpty() && notificationsBatch.size() <= 10) {
            notificationsBatch.add(notificationQueue.poll());
        }
        return notificationsBatch;
    }
}
