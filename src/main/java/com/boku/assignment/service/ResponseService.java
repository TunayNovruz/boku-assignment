package com.boku.assignment.service;

import com.boku.assignment.dto.response.ProviderResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ResponseService {

    private final ConcurrentLinkedQueue<ProviderResponse> responseQueue = new ConcurrentLinkedQueue<>();

    public void addResponse(ProviderResponse response) {
        responseQueue.add(response);
    }

    public List<ProviderResponse> getNextResponses() {
        List<ProviderResponse> responses = new ArrayList<>();
        while (!responseQueue.isEmpty() && responses.size() <= 10) {
            responses.add(responseQueue.poll());
        }
        return responses;
    }
}

