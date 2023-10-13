package com.novare.tredara.controllers;

import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.payloads.NotificationDTO;
import com.novare.tredara.services.LogService;
import com.novare.tredara.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController( NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<NotificationDTO>> getNotificationHistory(@Valid @RequestBody NotificationDTO notificationDTO) {
        return ResponseEntity.ok(notificationService.getFilteredNotificationHistory(notificationDTO));
    }
}
