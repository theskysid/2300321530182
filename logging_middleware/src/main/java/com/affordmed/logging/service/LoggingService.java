package com.affordmed.logging.service;

public interface LoggingService {
    void log(String stack, String level, String packageName, String message);
}
