package com.smartgrid.dto;

import java.util.List;

public class DashboardStatsDto {
    private Long totalConsumers;
    private Long totalGridNodes;
    private Long activeFaults;
    private Long activeOutages;
    private Double currentVoltage;
    private Double currentLoad;
    private Double totalEnergyConsumption;
    private List<AlertDto> recentAlerts;
    private List<MonthlyConsumption> monthlyConsumption;
    private List<FaultSeverityStat> faultSeverityStats;
    private List<LoadDistribution> loadDistribution;

    public DashboardStatsDto() {}

    public DashboardStatsDto(Long totalConsumers, Long totalGridNodes, Long activeFaults, Long activeOutages, 
                             Double currentVoltage, Double currentLoad, Double totalEnergyConsumption, 
                             List<AlertDto> recentAlerts, List<MonthlyConsumption> monthlyConsumption, 
                             List<FaultSeverityStat> faultSeverityStats, List<LoadDistribution> loadDistribution) {
        this.totalConsumers = totalConsumers;
        this.totalGridNodes = totalGridNodes;
        this.activeFaults = activeFaults;
        this.activeOutages = activeOutages;
        this.currentVoltage = currentVoltage;
        this.currentLoad = currentLoad;
        this.totalEnergyConsumption = totalEnergyConsumption;
        this.recentAlerts = recentAlerts;
        this.monthlyConsumption = monthlyConsumption;
        this.faultSeverityStats = faultSeverityStats;
        this.loadDistribution = loadDistribution;
    }

    public Long getTotalConsumers() { return totalConsumers; }
    public void setTotalConsumers(Long totalConsumers) { this.totalConsumers = totalConsumers; }

    public Long getTotalGridNodes() { return totalGridNodes; }
    public void setTotalGridNodes(Long totalGridNodes) { this.totalGridNodes = totalGridNodes; }

    public Long getActiveFaults() { return activeFaults; }
    public void setActiveFaults(Long activeFaults) { this.activeFaults = activeFaults; }

    public Long getActiveOutages() { return activeOutages; }
    public void setActiveOutages(Long activeOutages) { this.activeOutages = activeOutages; }

    public Double getCurrentVoltage() { return currentVoltage; }
    public void setCurrentVoltage(Double currentVoltage) { this.currentVoltage = currentVoltage; }

    public Double getCurrentLoad() { return currentLoad; }
    public void setCurrentLoad(Double currentLoad) { this.currentLoad = currentLoad; }

    public Double getTotalEnergyConsumption() { return totalEnergyConsumption; }
    public void setTotalEnergyConsumption(Double totalEnergyConsumption) { this.totalEnergyConsumption = totalEnergyConsumption; }

    public List<AlertDto> getRecentAlerts() { return recentAlerts; }
    public void setRecentAlerts(List<AlertDto> recentAlerts) { this.recentAlerts = recentAlerts; }

    public List<MonthlyConsumption> getMonthlyConsumption() { return monthlyConsumption; }
    public void setMonthlyConsumption(List<MonthlyConsumption> monthlyConsumption) { this.monthlyConsumption = monthlyConsumption; }

    public List<FaultSeverityStat> getFaultSeverityStats() { return faultSeverityStats; }
    public void setFaultSeverityStats(List<FaultSeverityStat> faultSeverityStats) { this.faultSeverityStats = faultSeverityStats; }

    public List<LoadDistribution> getLoadDistribution() { return loadDistribution; }
    public void setLoadDistribution(List<LoadDistribution> loadDistribution) { this.loadDistribution = loadDistribution; }

    public static DashboardStatsDtoBuilder builder() {
        return new DashboardStatsDtoBuilder();
    }

    public static class DashboardStatsDtoBuilder {
        private Long totalConsumers;
        private Long totalGridNodes;
        private Long activeFaults;
        private Long activeOutages;
        private Double currentVoltage;
        private Double currentLoad;
        private Double totalEnergyConsumption;
        private List<AlertDto> recentAlerts;
        private List<MonthlyConsumption> monthlyConsumption;
        private List<FaultSeverityStat> faultSeverityStats;
        private List<LoadDistribution> loadDistribution;

        public DashboardStatsDtoBuilder totalConsumers(Long totalConsumers) { this.totalConsumers = totalConsumers; return this; }
        public DashboardStatsDtoBuilder totalGridNodes(Long totalGridNodes) { this.totalGridNodes = totalGridNodes; return this; }
        public DashboardStatsDtoBuilder activeFaults(Long activeFaults) { this.activeFaults = activeFaults; return this; }
        public DashboardStatsDtoBuilder activeOutages(Long activeOutages) { this.activeOutages = activeOutages; return this; }
        public DashboardStatsDtoBuilder currentVoltage(Double currentVoltage) { this.currentVoltage = currentVoltage; return this; }
        public DashboardStatsDtoBuilder currentLoad(Double currentLoad) { this.currentLoad = currentLoad; return this; }
        public DashboardStatsDtoBuilder totalEnergyConsumption(Double totalEnergyConsumption) { this.totalEnergyConsumption = totalEnergyConsumption; return this; }
        public DashboardStatsDtoBuilder recentAlerts(List<AlertDto> recentAlerts) { this.recentAlerts = recentAlerts; return this; }
        public DashboardStatsDtoBuilder monthlyConsumption(List<MonthlyConsumption> monthlyConsumption) { this.monthlyConsumption = monthlyConsumption; return this; }
        public DashboardStatsDtoBuilder faultSeverityStats(List<FaultSeverityStat> faultSeverityStats) { this.faultSeverityStats = faultSeverityStats; return this; }
        public DashboardStatsDtoBuilder loadDistribution(List<LoadDistribution> loadDistribution) { this.loadDistribution = loadDistribution; return this; }

        public DashboardStatsDto build() {
            return new DashboardStatsDto(totalConsumers, totalGridNodes, activeFaults, activeOutages, currentVoltage, 
                                         currentLoad, totalEnergyConsumption, recentAlerts, monthlyConsumption, 
                                         faultSeverityStats, loadDistribution);
        }
    }

    public static class MonthlyConsumption {
        private String month;
        private Double consumption;

        public MonthlyConsumption() {}

        public MonthlyConsumption(String month, Double consumption) {
            this.month = month;
            this.consumption = consumption;
        }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public Double getConsumption() { return consumption; }
        public void setConsumption(Double consumption) { this.consumption = consumption; }
    }

    public static class FaultSeverityStat {
        private String severity;
        private Long count;

        public FaultSeverityStat() {}

        public FaultSeverityStat(String severity, Long count) {
            this.severity = severity;
            this.count = count;
        }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }

        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class LoadDistribution {
        private String zoneName;
        private Double currentLoad;
        private Double capacity;

        public LoadDistribution() {}

        public LoadDistribution(String zoneName, Double currentLoad, Double capacity) {
            this.zoneName = zoneName;
            this.currentLoad = currentLoad;
            this.capacity = capacity;
        }

        public String getZoneName() { return zoneName; }
        public void setZoneName(String zoneName) { this.zoneName = zoneName; }

        public Double getCurrentLoad() { return currentLoad; }
        public void setCurrentLoad(Double currentLoad) { this.currentLoad = currentLoad; }

        public Double getCapacity() { return capacity; }
        public void setCapacity(Double capacity) { this.capacity = capacity; }
    }
}
