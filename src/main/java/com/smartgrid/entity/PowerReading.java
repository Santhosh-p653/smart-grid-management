package com.smartgrid.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "power_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
