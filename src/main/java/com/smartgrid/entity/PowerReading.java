package com.smartgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "power_readings")
public class PowerReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double voltage; // Voltage in V

    @Column(nullable = false)
    private Double current; // Current in A

    @Column(nullable = false)
    private Double frequency; // Frequency in Hz

    @Column(name = "power_factor", nullable = false)
    private Double powerFactor; // Power factor (0.0 to 1.0)

    @Column(name = "active_load", nullable = false)
    private Double activeLoad; // Load in MW

    @Column(name = "health_status", nullable = false, length = 30)
    private String healthStatus; // NORMAL, WARNING, CRITICAL

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_node_id", nullable = false)
    private GridNode gridNode;

    public PowerReading() {}

    public PowerReading(Long id, Double voltage, Double current, Double frequency, Double powerFactor, Double activeLoad, String healthStatus, LocalDateTime timestamp, GridNode gridNode) {
        this.id = id;
        this.voltage = voltage;
        this.current = current;
        this.frequency = frequency;
        this.powerFactor = powerFactor;
        this.activeLoad = activeLoad;
        this.healthStatus = healthStatus;
        this.timestamp = timestamp;
        this.gridNode = gridNode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getVoltage() { return voltage; }
    public void setVoltage(Double voltage) { this.voltage = voltage; }

    public Double getCurrent() { return current; }
    public void setCurrent(Double current) { this.current = current; }

    public Double getFrequency() { return frequency; }
    public void setFrequency(Double frequency) { this.frequency = frequency; }

    public Double getPowerFactor() { return powerFactor; }
    public void setPowerFactor(Double powerFactor) { this.powerFactor = powerFactor; }

    public Double getActiveLoad() { return activeLoad; }
    public void setActiveLoad(Double activeLoad) { this.activeLoad = activeLoad; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public GridNode getGridNode() { return gridNode; }
    public void setGridNode(GridNode gridNode) { this.gridNode = gridNode; }

    // Manual Builder implementation to satisfy Builder pattern usage
    public static PowerReadingBuilder builder() {
        return new PowerReadingBuilder();
    }

    public static class PowerReadingBuilder {
        private Long id;
        private Double voltage;
        private Double current;
        private Double frequency;
        private Double powerFactor;
        private Double activeLoad;
        private String healthStatus;
        private LocalDateTime timestamp;
        private GridNode gridNode;

        public PowerReadingBuilder id(Long id) { this.id = id; return this; }
        public PowerReadingBuilder voltage(Double voltage) { this.voltage = voltage; return this; }
        public PowerReadingBuilder current(Double current) { this.current = current; return this; }
        public PowerReadingBuilder frequency(Double frequency) { this.frequency = frequency; return this; }
        public PowerReadingBuilder powerFactor(Double powerFactor) { this.powerFactor = powerFactor; return this; }
        public PowerReadingBuilder activeLoad(Double activeLoad) { this.activeLoad = activeLoad; return this; }
        public PowerReadingBuilder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public PowerReadingBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public PowerReadingBuilder gridNode(GridNode gridNode) { this.gridNode = gridNode; return this; }

        public PowerReading build() {
            return new PowerReading(id, voltage, current, frequency, powerFactor, activeLoad, healthStatus, timestamp, gridNode);
        }
    }
}
