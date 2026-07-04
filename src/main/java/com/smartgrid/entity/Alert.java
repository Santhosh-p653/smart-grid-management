package com.smartgrid.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
