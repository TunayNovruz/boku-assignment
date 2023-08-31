package com.boku.assignment.service;

import com.boku.assignment.dto.request.NotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testGetNextNotifications() {
        NotificationDto notificationDto1 = new NotificationDto();
        notificationDto1.setText("Test Message 1");
        NotificationDto notificationDto2 = new NotificationDto();
        notificationDto2.setText("Test Message 2");
        notificationService.addNotification(notificationDto1);
        notificationService.addNotification(notificationDto2);
        List<NotificationDto> notifications = notificationService.getNextNotifications();
        assertEquals(2, notifications.size());
        assertEquals(notificationDto1, notifications.get(0));
        assertEquals(notificationDto2, notifications.get(1));
    }
}
