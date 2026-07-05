package com.smartgrid.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grid_nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GridNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 50)
    private String type; // Substation, Transformer, Distribution Box
    @Column(nullable = false)
    private Double capacity; // Capacity in MW
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NodeStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
    @OneToMany(mappedBy = "gridNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PowerReading> powerReadings = new ArrayList<>();
    @OneToMany(mappedBy = "gridNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Fault> faults = new ArrayList<>();
    @OneToMany(mappedBy = "gridNode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Outage> outages = new ArrayList<>();
}