package com.smartgrid.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "outages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
