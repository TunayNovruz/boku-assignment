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
    //todo we can decrease/increase depending on the load and resources
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); //todo move to constants/config

    @Async
    @Scheduled(fixedRate = 1000) // process 10 job in 100 ms
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
        ResponseEntity<MerchantResponse> responseEntity = merchantService.sendNotification(notification);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            MerchantResponse merchantResponse = responseEntity.getBody();
            if (merchantResponse != null) {
                responseService.addResponse(new ProviderResponse(merchantResponse,notification));
            }
        } else {
            //todo retry strategy
            // we can try at least 3 times then we can move to other queue/ or store in db
            notificationService.addNotification(notification); //adding to end of the queue
        }
    }
}
