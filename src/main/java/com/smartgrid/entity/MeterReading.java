package com.smartgrid.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @Column(name = "reading_date", nullable = false)
    private LocalDateTime readingDate;

    @Column(name = "active_power", nullable = false)
    private Double activePower; // Active consumption in kWh

    @Column(name = "reactive_power", nullable = false)
    private Double reactivePower; // Reactive consumption in kVARh

    @Column(name = "billing_amount", nullable = false)
    private Double billingAmount; // In currency units

    @Column(nullable = false, length = 30)
    private String status; // BILLED, UNBILLED
}
