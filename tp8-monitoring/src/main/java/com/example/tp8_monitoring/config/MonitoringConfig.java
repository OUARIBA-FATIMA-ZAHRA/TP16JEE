package com.example.tp8_monitoring.config;




import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class MonitoringConfig {

    private final MeterRegistry meterRegistry;
    private boolean metricsRegistered = false;

    public MonitoringConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {
        if (!metricsRegistered) {
            new JvmMemoryMetrics().bindTo(meterRegistry);
            new ProcessorMetrics().bindTo(meterRegistry);
            metricsRegistered = true;
        }
    }
}