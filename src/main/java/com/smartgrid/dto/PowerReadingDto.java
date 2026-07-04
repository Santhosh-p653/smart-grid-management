package com.smartgrid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PowerReadingDto {
    private Long id;
    private Long gridNodeId;
    private String gridNodeName;
    private Double voltage;
    private Double current;
    private Double frequency;
    private Double powerFactor;
    private Double activeLoad;
    private String healthStatus;
    private LocalDateTime timestamp;
}
