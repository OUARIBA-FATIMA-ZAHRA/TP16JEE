package com.example.tp8_monitoring.service;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DataProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorService.class);
    private final Counter processingCounter;
    private final Timer processingTimer;
    private final Counter errorCounter;

    public DataProcessorService(MeterRegistry meterRegistry) {
        this.processingCounter = Counter.builder("data.processing.total")
                .description("Total number of data processing operations")
                .register(meterRegistry);

        this.processingTimer = Timer.builder("data.processing.duration")
                .description("Time taken to process data")
                .register(meterRegistry);

        this.errorCounter = Counter.builder("data.processing.errors")
                .description("Number of processing errors")
                .register(meterRegistry);
    }

    public Map<String, Object> executeProcessing(int durationMs) {
        long startTime = System.nanoTime();
        processingCounter.increment();

        logger.info("Starting data processing operation with duration: {} ms", durationMs);

        Map<String, Object> response = new HashMap<>();
        response.put("operationId", System.currentTimeMillis());
        response.put("requestedDuration", durationMs);

        try {
            if (durationMs > 5000) {
                logger.warn("Processing duration exceeds recommended threshold: {} ms", durationMs);
            }

            Thread.sleep(durationMs);

            response.put("status", "SUCCESS");
            response.put("actualDuration", durationMs);
            logger.info("Data processing completed successfully in {} ms", durationMs);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            errorCounter.increment();
            response.put("status", "INTERRUPTED");
            response.put("error", e.getMessage());
            logger.error("Processing interrupted after {} ms", durationMs, e);
        }

        long processingNanos = System.nanoTime() - startTime;
        processingTimer.record(processingNanos, TimeUnit.NANOSECONDS);

        return response;
    }

    public ResponseEntity<Map<String, String>> simulateProcessingError() {
        errorCounter.increment();
        logger.error("Simulated error occurred during data processing");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Processing failure");
        errorResponse.put("timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}