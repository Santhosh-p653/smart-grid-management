package com.smartgrid.dto;

import java.time.LocalDateTime;

public class AlertDto {
    private Long id;
    private String title;
    private String message;
    private String severity;
    private String status;
    private LocalDateTime createdAt;

    public AlertDto() {}

    public AlertDto(Long id, String title, String message, String severity, String status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AlertDtoBuilder builder() {
        return new AlertDtoBuilder();
    }

    public static class AlertDtoBuilder {
        private Long id;
        private String title;
        private String message;
        private String severity;
        private String status;
        private LocalDateTime createdAt;

        public AlertDtoBuilder id(Long id) { this.id = id; return this; }
        public AlertDtoBuilder title(String title) { this.title = title; return this; }
        public AlertDtoBuilder message(String message) { this.message = message; return this; }
        public AlertDtoBuilder severity(String severity) { this.severity = severity; return this; }
        public AlertDtoBuilder status(String status) { this.status = status; return this; }
        public AlertDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AlertDto build() {
            return new AlertDto(id, title, message, severity, status, createdAt);
        }
    }
}
