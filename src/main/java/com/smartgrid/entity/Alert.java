package com.smartgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(nullable = false, length = 30)
    private String severity; // INFO, WARNING, CRITICAL

    @Column(nullable = false, length = 30)
    private String status; // UNREAD, READ

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Alert() {}

    public Alert(Long id, String title, String message, String severity, String status, LocalDateTime createdAt) {
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

    public static AlertBuilder builder() {
        return new AlertBuilder();
    }

    public static class AlertBuilder {
        private Long id;
        private String title;
        private String message;
        private String severity;
        private String status;
        private LocalDateTime createdAt;

        public AlertBuilder id(Long id) { this.id = id; return this; }
        public AlertBuilder title(String title) { this.title = title; return this; }
        public AlertBuilder message(String message) { this.message = message; return this; }
        public AlertBuilder severity(String severity) { this.severity = severity; return this; }
        public AlertBuilder status(String status) { this.status = status; return this; }
        public AlertBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Alert build() {
            return new Alert(id, title, message, severity, status, createdAt);
        }
    }
}
