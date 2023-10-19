package com.novare.tredara.controllers;


import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.services.LogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/")
    public ResponseEntity<List<LogDTO>> getLogHistory(@Valid @RequestBody LogDTO logDTO) {
        return ResponseEntity.ok(logService.getFilteredLogHistory(logDTO));
    }
}
