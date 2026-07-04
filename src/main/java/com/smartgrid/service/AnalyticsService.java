package com.smartgrid.service;

import com.smartgrid.dto.AlertDto;
import com.smartgrid.dto.DashboardStatsDto;
import com.smartgrid.dto.ReportDto;
import com.smartgrid.entity.*;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private FaultRepository faultRepository;

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private PowerReadingRepository powerReadingRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardStatsDto getDashboardStats() {
        long totalConsumers = consumerRepository.count();
        long totalGridNodes = gridNodeRepository.count();
        long activeFaults = faultRepository.countByStatus(FaultStatus.ACTIVE);
        long activeOutages = outageRepository.countByStatus("ACTIVE");

        // Aggregated live telemetry
        List<GridNode> nodes = gridNodeRepository.findAll();
        double sumVoltage = 0.0;
        double sumLoad = 0.0;
        int activeNodeCount = 0;

        for (GridNode node : nodes) {
            if (node.getStatus() == NodeStatus.ACTIVE) {
                activeNodeCount++;
                PowerReading reading = powerReadingRepository.findFirstByGridNodeIdOrderByTimestampDesc(node.getId()).orElse(null);
                if (reading != null) {
                    sumVoltage += reading.getVoltage();
                    sumLoad += reading.getActiveLoad();
                } else {
                    sumVoltage += 230.0; // standard default
                }
            }
        }

        double avgVoltage = activeNodeCount > 0 ? (sumVoltage / activeNodeCount) : 230.0;
        double totalConsumption = meterReadingRepository.sumAllActivePower();

        // Recent alerts
        List<AlertDto> recentAlerts = alertRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(a -> AlertDto.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .message(a.getMessage())
                        .severity(a.getSeverity())
                        .status(a.getStatus())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // Simulated Monthly Consumption (for chart illustration)
        List<DashboardStatsDto.MonthlyConsumption> monthlyConsumptions = new ArrayList<>();
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("Jan", totalConsumption * 0.12));
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("Feb", totalConsumption * 0.10));
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("Mar", totalConsumption * 0.14));
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("Apr", totalConsumption * 0.16));
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("May", totalConsumption * 0.22));
        monthlyConsumptions.add(new DashboardStatsDto.MonthlyConsumption("Jun", totalConsumption * 0.26));

        // Fault severity stats
        List<DashboardStatsDto.FaultSeverityStat> severityStats = new ArrayList<>();
        for (FaultSeverity s : FaultSeverity.values()) {
            // Count faults of this severity
            long count = faultRepository.findAll().stream()
                    .filter(f -> f.getSeverity() == s)
                    .count();
            severityStats.add(new DashboardStatsDto.FaultSeverityStat(s.name(), count));
        }

        // Zone-wise load distribution
        List<DashboardStatsDto.LoadDistribution> loadDistribution = new ArrayList<>();
        Map<String, Double[]> zonePower = new HashMap<>(); // [currentLoad, capacity]
        for (GridNode node : nodes) {
            String zoneName = node.getZone().getName();
            double load = powerReadingRepository.findFirstByGridNodeIdOrderByTimestampDesc(node.getId())
                    .map(PowerReading::getActiveLoad)
                    .orElse(0.0);
            Double[] stats = zonePower.getOrDefault(zoneName, new Double[]{0.0, 0.0});
            stats[0] += load;
            stats[1] += node.getCapacity();
            zonePower.put(zoneName, stats);
        }
        for (Map.Entry<String, Double[]> entry : zonePower.entrySet()) {
            loadDistribution.add(new DashboardStatsDto.LoadDistribution(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }

        return DashboardStatsDto.builder()
                .totalConsumers(totalConsumers)
                .totalGridNodes(totalGridNodes)
                .activeFaults(activeFaults)
                .activeOutages(activeOutages)
                .currentVoltage(avgVoltage)
                .currentLoad(sumLoad)
                .totalEnergyConsumption(totalConsumption)
                .recentAlerts(recentAlerts)
                .monthlyConsumption(monthlyConsumptions)
                .faultSeverityStats(severityStats)
                .loadDistribution(loadDistribution)
                .build();
    }

    @Transactional
    public ReportDto generateReport(String typeStr, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Generating user not found"));

        ReportType type;
        try {
            type = ReportType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Report Type. Must be GRID_HEALTH, POWER_DISTRIBUTION, CONSUMPTION, OUTAGE, or FAULT");
        }

        String title = typeStr.replace("_", " ") + " Report - " + LocalDateTime.now().getDayOfMonth() + " " + LocalDateTime.now().getMonth().name();
        
        // Generate content dynamically depending on the report type
        StringBuilder content = new StringBuilder();
        content.append(String.format("=========================================\n"));
        content.append(String.format("%s\n", title.toUpperCase()));
        content.append(String.format("Generated At: %s\n", LocalDateTime.now()));
        content.append(String.format("Generated By: %s (%s)\n", user.getFullName(), user.getRole().getName()));
        content.append(String.format("=========================================\n\n"));

        switch (type) {
            case GRID_HEALTH:
                content.append("GRID STATUS REPORT:\n");
                content.append(String.format("Total Grid Nodes: %d\n", gridNodeRepository.count()));
                content.append(String.format("Active Faults: %d\n", faultRepository.countByStatus(FaultStatus.ACTIVE)));
                content.append(String.format("Active Outages: %d\n", outageRepository.countByStatus("ACTIVE")));
                break;
            case POWER_DISTRIBUTION:
                content.append("POWER DISTRIBUTION MONITORING ANALYSIS:\n");
                gridNodeRepository.findAll().forEach(node -> {
                    double load = powerReadingRepository.findFirstByGridNodeIdOrderByTimestampDesc(node.getId())
                            .map(PowerReading::getActiveLoad).orElse(0.0);
                    content.append(String.format("Node: %s | Capacity: %.2f MW | Active Load: %.2f MW (Ratio: %.1f%%) | Status: %s\n",
                            node.getName(), node.getCapacity(), load, (load / node.getCapacity()) * 100, node.getStatus()));
                });
                break;
            case CONSUMPTION:
                content.append("CONSUMER ENERGY CONSUMPTION REPORT:\n");
                content.append(String.format("Total Registered Consumers: %d\n", consumerRepository.count()));
                content.append(String.format("Total Cumulative Active Power: %.2f kWh\n", meterReadingRepository.sumAllActivePower()));
                break;
            case OUTAGE:
                content.append("OUTAGE TRACKING HISTORY REPORT:\n");
                content.append(String.format("Active Outages Count: %d\n", outageRepository.countByStatus("ACTIVE")));
                outageRepository.findAll().forEach(o -> {
                    content.append(String.format("Node: %s | Started: %s | Restored: %s | Status: %s | Description: %s\n",
                            o.getGridNode().getName(), o.getStartTime(), o.getEndTime() != null ? o.getEndTime() : "N/A", o.getStatus(), o.getDescription()));
                });
                break;
            case FAULT:
                content.append("HARDWARE FAULT LOGS REPORT:\n");
                content.append(String.format("Active Faults Count: %d\n", faultRepository.countByStatus(FaultStatus.ACTIVE)));
                faultRepository.findAll().forEach(f -> {
                    content.append(String.format("ID: %d | Node: %s | Title: %s | Severity: %s | Status: %s | Reported: %s\n",
                            f.getId(), f.getGridNode().getName(), f.getTitle(), f.getSeverity(), f.getStatus(), f.getReportedAt()));
                });
                break;
        }

        Report report = Report.builder()
                .title(title)
                .type(type)
                .generatedAt(LocalDateTime.now())
                .generatedBy(user)
                .content(content.toString())
                .build();

        report = reportRepository.save(report);
        return mapToReportDto(report);
    }

    public List<ReportDto> getReports() {
        return reportRepository.findAll().stream()
                .map(this::mapToReportDto)
                .collect(Collectors.toList());
    }

    public ReportDto getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return mapToReportDto(report);
    }

    private ReportDto mapToReportDto(Report r) {
        return ReportDto.builder()
                .id(r.getId())
                .title(r.getTitle())
                .type(r.getType().name())
                .generatedAt(r.getGeneratedAt())
                .generatedByUsername(r.getGeneratedBy() != null ? r.getGeneratedBy().getUsername() : "SYSTEM")
                .content(r.getContent())
                .build();
    }
}
