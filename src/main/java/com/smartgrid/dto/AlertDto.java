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
public class AlertDto {
    private Long id;
    private String title;
    private String message;
    private String severity;
    private String status;
    private LocalDateTime createdAt;
}
