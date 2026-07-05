package com.smartgrid.dto;

import java.time.LocalDateTime;

public class ReportDto {
    private Long id;
    private String title;
    private String type;
    private LocalDateTime generatedAt;
    private String generatedByUsername;
    private String content;

    public ReportDto() {}

    public ReportDto(Long id, String title, String type, LocalDateTime generatedAt, String generatedByUsername, String content) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.generatedAt = generatedAt;
        this.generatedByUsername = generatedByUsername;
        this.content = content;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public String getGeneratedByUsername() { return generatedByUsername; }
    public void setGeneratedByUsername(String generatedByUsername) { this.generatedByUsername = generatedByUsername; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public static ReportDtoBuilder builder() {
        return new ReportDtoBuilder();
    }

    public static class ReportDtoBuilder {
        private Long id;
        private String title;
        private String type;
        private LocalDateTime generatedAt;
        private String generatedByUsername;
        private String content;

        public ReportDtoBuilder id(Long id) { this.id = id; return this; }
        public ReportDtoBuilder title(String title) { this.title = title; return this; }
        public ReportDtoBuilder type(String type) { this.type = type; return this; }
        public ReportDtoBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public ReportDtoBuilder generatedByUsername(String generatedByUsername) { this.generatedByUsername = generatedByUsername; return this; }
        public ReportDtoBuilder content(String content) { this.content = content; return this; }

        public ReportDto build() {
            return new ReportDto(id, title, type, generatedAt, generatedByUsername, content);
        }
    }
}
