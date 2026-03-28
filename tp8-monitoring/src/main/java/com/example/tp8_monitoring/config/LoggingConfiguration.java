package com.example.tp8_monitoring.config;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class LoggingConfiguration {

    @PostConstruct
    public void configureLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
        consoleEncoder.setContext(loggerContext);
        consoleEncoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        consoleEncoder.start();

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.start();

        rootLogger.addAppender(consoleAppender);
    }
}