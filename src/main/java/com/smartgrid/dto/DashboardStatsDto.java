package com.smartgrid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private Long totalConsumers;
    private Long totalGridNodes;
    private Long activeFaults;
    private Long activeOutages;
    private Double currentVoltage; // average voltage
    private Double currentLoad; // sum load (MW)
    private Double totalEnergyConsumption; // sum kWh
    private List<AlertDto> recentAlerts;
    
    // Series data for dashboard charts
    private List<MonthlyConsumption> monthlyConsumption;
    private List<FaultSeverityStat> faultSeverityStats;
    private List<LoadDistribution> loadDistribution;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyConsumption {
        private String month;
        private Double consumption; // kWh
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FaultSeverityStat {
        private String severity;
        private Long count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoadDistribution {
        private String zoneName;
        private Double currentLoad;
        private Double capacity;
    }
}
