package com.affordmed.logging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LogRequest {

    @NotBlank(message = "Stack cannot be blank")
    @Pattern(regexp = "^(backend|frontend)$", message = "Stack must be either backend or frontend")
    private String stack;

    @NotBlank(message = "Level cannot be blank")
    @Pattern(regexp = "^(debug|info|warn|error|fatal)$", message = "Level must be debug, info, warn, error, or fatal")
    private String level;

    @NotBlank(message = "Package name cannot be blank")
    @Pattern(regexp = "^(cache|controller|cron_job|db|domain|handler|repository|route|service)$", 
             message = "Invalid package name")
    private String packageName;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    public LogRequest() {
    }

    public LogRequest(String stack, String level, String packageName, String message) {
        this.stack = stack;
        this.level = level;
        this.packageName = packageName;
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
