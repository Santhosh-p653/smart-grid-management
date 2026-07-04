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
public class OutageDto {
    private Long id;
    private Long gridNodeId;
    private String gridNodeName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String description;
}
