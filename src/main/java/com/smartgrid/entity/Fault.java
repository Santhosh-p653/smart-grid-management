package com.smartgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "faults")
public class Fault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_node_id", nullable = false)
    private GridNode gridNode;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FaultSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FaultStatus status;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public Fault() {}

    public Fault(Long id, GridNode gridNode, String title, String description, FaultSeverity severity, FaultStatus status, LocalDateTime reportedAt, LocalDateTime resolvedAt) {
        this.id = id;
        this.gridNode = gridNode;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.reportedAt = reportedAt;
        this.resolvedAt = resolvedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GridNode getGridNode() { return gridNode; }
    public void setGridNode(GridNode gridNode) { this.gridNode = gridNode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public FaultSeverity getSeverity() { return severity; }
    public void setSeverity(FaultSeverity severity) { this.severity = severity; }

    public FaultStatus getStatus() { return status; }
    public void setStatus(FaultStatus status) { this.status = status; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public static FaultBuilder builder() {
        return new FaultBuilder();
    }

    public static class FaultBuilder {
        private Long id;
        private GridNode gridNode;
        private String title;
        private String description;
        private FaultSeverity severity;
        private FaultStatus status;
        private LocalDateTime reportedAt;
        private LocalDateTime resolvedAt;

        public FaultBuilder id(Long id) { this.id = id; return this; }
        public FaultBuilder gridNode(GridNode gridNode) { this.gridNode = gridNode; return this; }
        public FaultBuilder title(String title) { this.title = title; return this; }
        public FaultBuilder description(String description) { this.description = description; return this; }
        public FaultBuilder severity(FaultSeverity severity) { this.severity = severity; return this; }
        public FaultBuilder status(FaultStatus status) { this.status = status; return this; }
        public FaultBuilder reportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; return this; }
        public FaultBuilder resolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }

        public Fault build() {
            return new Fault(id, gridNode, title, description, severity, status, reportedAt, resolvedAt);
        }
    }
}
