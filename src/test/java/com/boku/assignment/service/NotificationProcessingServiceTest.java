package com.boku.assignment.service;

import com.boku.assignment.dto.request.NotificationDto;
import com.boku.assignment.dto.response.ProviderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationProcessingServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private MerchantService merchantService;

    @Mock
    private ResponseService responseService;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private NotificationProcessingService notificationProcessingService;

    @Test
    public void testProcessNotifications_NoNotifications() {
        when(notificationService.getNextNotifications()).thenReturn(null);
        notificationProcessingService.processNotifications();
        verify(notificationService, times(1)).getNextNotifications();
        verifyNoInteractions(executorService, merchantService, responseService);
    }

    @Test
    public void testProcessNotifications_HasNotifications() {
        List<NotificationDto> notifications = new ArrayList<>();
        notifications.add(new NotificationDto());
        when(notificationService.getNextNotifications()).thenReturn(notifications);
        notificationProcessingService.processNotifications();
        verify(notificationService, times(1)).getNextNotifications();
        verify(responseService, times(1)).addResponse(any(ProviderResponse.class));
    }


    @Test
    public void testProcessNotification_Failure() {
        notificationProcessingService.processNotifications();
        verify(merchantService, times(0)).sendNotification(any());
        verify(responseService, times(0)).addResponse(any(ProviderResponse.class));
    }
}
