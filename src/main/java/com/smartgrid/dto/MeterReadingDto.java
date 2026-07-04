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
public class MeterReadingDto {
    private Long id;
    private Long consumerId;
    private String consumerName;
    private LocalDateTime readingDate;
    private Double activePower;
    private Double reactivePower;
    private Double billingAmount;
    private String status;
}
