package com.smartgrid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridNodeDto {
    private Long id;
    private String name;
    private String type;
    private Double capacity; // MW
    private String status; // ACTIVE, INACTIVE, UNDER_MAINTENANCE
    private Long zoneId;
    private String zoneName;
    
    // Live statuses
    private Double currentVoltage;
    private Double currentLoad;
    private String currentHealth;
}
