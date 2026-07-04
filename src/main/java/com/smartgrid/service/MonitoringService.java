package com.smartgrid.service;

import com.smartgrid.dto.PowerReadingDto;
import com.smartgrid.entity.Alert;
import com.smartgrid.entity.GridNode;
import com.smartgrid.entity.PowerReading;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.AlertRepository;
import com.smartgrid.repository.GridNodeRepository;
import com.smartgrid.repository.PowerReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    @Autowired
    private PowerReadingRepository powerReadingRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Transactional
    public PowerReadingDto recordReading(PowerReadingDto dto) {
        GridNode node = gridNodeRepository.findById(dto.getGridNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));

        String health = determineHealth(dto.getVoltage(), dto.getActiveLoad(), node.getCapacity());

        PowerReading reading = PowerReading.builder()
                .gridNode(node)
                .voltage(dto.getVoltage())
                .current(dto.getCurrent())
                .frequency(dto.getFrequency())
                .powerFactor(dto.getPowerFactor())
                .activeLoad(dto.getActiveLoad())
                .healthStatus(health)
                .timestamp(LocalDateTime.now())
                .build();

        reading = powerReadingRepository.save(reading);

        // Check if we should trigger alerts
        checkAndTriggerAlerts(node, reading);

        return mapToDto(reading);
    }

    public List<PowerReadingDto> getReadingsHistory(Long gridNodeId, int limit) {
        Pageable limitPageable = PageRequest.of(0, limit);
        return powerReadingRepository.findByGridNodeIdOrderByTimestampDesc(gridNodeId, limitPageable).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public String determineHealth(Double voltage, Double load, Double capacity) {
        // Voltage warnings: normal is ~230V. Allow 10% variance (207V - 253V)
        boolean voltageAbnormal = voltage < 200 || voltage > 260;
        boolean voltageCritical = voltage < 180 || voltage > 280;

        // Overload warnings
        double loadRatio = load / capacity;
        boolean overloaded = loadRatio >= 1.0;
        boolean nearOverload = loadRatio >= 0.85;

        if (voltageCritical || overloaded) {
            return "CRITICAL";
        } else if (voltageAbnormal || nearOverload) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }

    private void checkAndTriggerAlerts(GridNode node, PowerReading reading) {
        if ("CRITICAL".equals(reading.getHealthStatus())) {
            double loadRatio = reading.getActiveLoad() / node.getCapacity();
            String title = "Critical Alert: " + node.getName();
            String message = String.format("Node %s is at CRITICAL state. Telemetry: Voltage=%.1fV, Load=%.2fMW (Capacity=%.1fMW, Ratio=%.1f%%)",
                    node.getName(), reading.getVoltage(), reading.getActiveLoad(), node.getCapacity(), loadRatio * 100);

            alertRepository.save(Alert.builder()
                    .title(title)
                    .message(message)
                    .severity("CRITICAL")
                    .status("UNREAD")
                    .createdAt(LocalDateTime.now())
                    .build());
        } else if ("WARNING".equals(reading.getHealthStatus())) {
            String title = "Warning Alert: " + node.getName();
            String message = String.format("Node %s is reporting WARNING status. Telemetry: Voltage=%.1fV, Load=%.2fMW",
                    node.getName(), reading.getVoltage(), reading.getActiveLoad());

            alertRepository.save(Alert.builder()
                    .title(title)
                    .message(message)
                    .severity("WARNING")
                    .status("UNREAD")
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }

    private PowerReadingDto mapToDto(PowerReading r) {
        return PowerReadingDto.builder()
                .id(r.getId())
                .gridNodeId(r.getGridNode().getId())
                .gridNodeName(r.getGridNode().getName())
                .voltage(r.getVoltage())
                .current(r.getCurrent())
                .frequency(r.getFrequency())
                .powerFactor(r.getPowerFactor())
                .activeLoad(r.getActiveLoad())
                .healthStatus(r.getHealthStatus())
                .timestamp(r.getTimestamp())
                .build();
    }
}
