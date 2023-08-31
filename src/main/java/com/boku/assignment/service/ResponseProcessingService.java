package com.boku.assignment.service;

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
public class ResponseProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(ResponseProcessingService.class);
    private final ResponseService responseService;
    private final ProviderService providerService;
    //todo we can decrease/increase depending on the load and resources
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); //todo move to constants/config

    @Async
    @Scheduled(fixedRate = 1000) // process 10 job in 100 ms
    public void processNotifications() {
        List<ProviderResponse> responses = responseService.getNextResponses();
        if (responses == null || responses.size() == 0) return;
        processBatch(responses);
    }


    private void processBatch(List<ProviderResponse> responses) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ProviderResponse response : responses) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processResponse(response), executorService);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processResponse(ProviderResponse response) {
        try {
            ResponseEntity<Void> responseEntity = providerService.sendResponse(response);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                //todo retry strategy or Dead Letter Queue
                responseService.addResponse(response); //adding to end of the queue again
            }
        }catch (Exception e){
            responseService.addResponse(response);
            logger.error(e.getMessage());
        }
    }
}


