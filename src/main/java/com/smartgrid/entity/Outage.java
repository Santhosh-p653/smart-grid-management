package com.smartgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "outages")
public class Outage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_node_id", nullable = false)
    private GridNode gridNode;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false, length = 30)
    private String status; // ACTIVE, RESTORED

    @Column(length = 255)
    private String description;

    public Outage() {}

    public Outage(Long id, GridNode gridNode, LocalDateTime startTime, LocalDateTime endTime, String status, String description) {
        this.id = id;
        this.gridNode = gridNode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GridNode getGridNode() { return gridNode; }
    public void setGridNode(GridNode gridNode) { this.gridNode = gridNode; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static OutageBuilder builder() {
        return new OutageBuilder();
    }

    public static class OutageBuilder {
        private Long id;
        private GridNode gridNode;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;
        private String description;

        public OutageBuilder id(Long id) { this.id = id; return this; }
        public OutageBuilder gridNode(GridNode gridNode) { this.gridNode = gridNode; return this; }
        public OutageBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public OutageBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public OutageBuilder status(String status) { this.status = status; return this; }
        public OutageBuilder description(String description) { this.description = description; return this; }

        public Outage build() {
            return new Outage(id, gridNode, startTime, endTime, status, description);
        }
    }
}
