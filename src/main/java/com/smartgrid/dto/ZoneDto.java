package com.smartgrid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDto {
    private Long id;
    private String name;
    private String description;
    private String region;
    private int nodeCount;
}
