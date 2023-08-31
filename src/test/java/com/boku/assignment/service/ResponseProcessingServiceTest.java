package com.boku.assignment.service;

import com.boku.assignment.dto.response.ProviderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseProcessingServiceTest {

    @Mock
    private ResponseService responseService;

    @Mock
    private ProviderService providerService;

    @InjectMocks
    private ResponseProcessingService service;

    @Test
    public void testProcessNotifications() {
        List<ProviderResponse> responses = new ArrayList<>();
        ProviderResponse response = new ProviderResponse();
        response.setMessage("test");
        response.setOperator("test");
        responses.add(response);

        when(responseService.getNextResponses()).thenReturn(responses);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> service.processNotifications());
        future.join();

        verify(providerService, times(1)).sendResponse(response);
    }
}