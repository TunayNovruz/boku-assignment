package com.boku.assignment.controller;

import com.boku.assignment.dto.request.NotificationDto;
import com.boku.assignment.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<String> addNotification(@ModelAttribute NotificationDto notificationDto) {
        try {
            notificationService.addNotification(notificationDto);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment notification"); //todo hardcoded text
        }
    }
}