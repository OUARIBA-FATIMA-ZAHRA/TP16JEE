package com.example.tp8_monitoring.controller;


import com.example.tp8_monitoring.service.DataProcessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    private final DataProcessorService dataProcessorService;

    public DataController(DataProcessorService dataProcessorService) {
        this.dataProcessorService = dataProcessorService;
    }

    @GetMapping("/process/{duration}")
    public ResponseEntity<Map<String, Object>> processData(@PathVariable int duration) {
        Map<String, Object> result = dataProcessorService.executeProcessing(duration);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health-check")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "operational", "timestamp",
                String.valueOf(System.currentTimeMillis())));
    }

    @GetMapping("/simulate-error")
    public ResponseEntity<Map<String, String>> simulateError() {
        return dataProcessorService.simulateProcessingError();
    }
}
