package com.novare.tredara.controllers;

import com.novare.tredara.models.Log;
import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/")
    public ResponseEntity<List<LogDTO>> getLogHistory() {
        List<LogDTO> logHistory = logService.getLogHistory().stream()
                .map(logService::convertToLogDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logHistory);
    }
}
