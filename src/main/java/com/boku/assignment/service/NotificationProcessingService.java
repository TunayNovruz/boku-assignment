package com.boku.assignment.service;

import com.boku.assignment.dto.request.NotificationDto;
import com.boku.assignment.dto.response.MerchantResponse;
import com.boku.assignment.dto.response.ProviderResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@EnableAsync
public class NotificationProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationProcessingService.class);
    private final NotificationService notificationService;
    private final MerchantService merchantService;
    private final ResponseService responseService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Async
    @Scheduled(fixedRate = 100)
    public void processNotifications() {
        List<NotificationDto> notifications = notificationService.getNextNotifications();
        if (notifications == null || notifications.size() == 0) return;
        processBatch(notifications);
    }


    private void processBatch(List<NotificationDto> notifications) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (NotificationDto notification : notifications) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processNotification(notification), executorService);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processNotification(NotificationDto notification) {
        //todo hardcoded text
        ProviderResponse response =
                new ProviderResponse(notification
                        , "Something went wrong. Please contact us at cs.boku.com to receive your service");
        try {
            ResponseEntity<MerchantResponse> responseEntity = merchantService.sendNotification(notification);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                MerchantResponse merchantResponse = responseEntity.getBody();
                if (merchantResponse != null) {
                    response.setMessage(merchantResponse.getReply_message());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            responseService.addResponse(response);
        }
    }
}
