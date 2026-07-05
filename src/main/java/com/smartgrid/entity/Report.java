package com.smartgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportType type;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User generatedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // JSON string or text summary content

    public Report() {}

    public Report(Long id, String title, ReportType type, LocalDateTime generatedAt, User generatedBy, String content) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
        this.content = content;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public User getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(User generatedBy) { this.generatedBy = generatedBy; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public static ReportBuilder builder() {
        return new ReportBuilder();
    }

    public static class ReportBuilder {
        private Long id;
        private String title;
        private ReportType type;
        private LocalDateTime generatedAt;
        private User generatedBy;
        private String content;

        public ReportBuilder id(Long id) { this.id = id; return this; }
        public ReportBuilder title(String title) { this.title = title; return this; }
        public ReportBuilder type(ReportType type) { this.type = type; return this; }
        public ReportBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public ReportBuilder generatedBy(User generatedBy) { this.generatedBy = generatedBy; return this; }
        public ReportBuilder content(String content) { this.content = content; return this; }

        public Report build() {
            return new Report(id, title, type, generatedAt, generatedBy, content);
        }
    }
}
