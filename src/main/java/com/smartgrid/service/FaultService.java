package com.smartgrid.service;

import com.smartgrid.dto.AlertDto;
import com.smartgrid.dto.FaultDto;
import com.smartgrid.entity.*;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.AlertRepository;
import com.smartgrid.repository.FaultRepository;
import com.smartgrid.repository.GridNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaultService {

    @Autowired
    private FaultRepository faultRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Transactional
    public FaultDto reportFault(FaultDto dto) {
        GridNode node = gridNodeRepository.findById(dto.getGridNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));

        FaultSeverity severity;
        try {
            severity = FaultSeverity.valueOf(dto.getSeverity().toUpperCase());
        } catch (IllegalArgumentException e) {
            severity = FaultSeverity.MEDIUM;
        }

        Fault fault = Fault.builder()
                .gridNode(node)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .severity(severity)
                .status(FaultStatus.ACTIVE)
                .reportedAt(LocalDateTime.now())
                .build();

        fault = faultRepository.save(fault);

        // Put node under maintenance if fault is high/critical
        if (severity == FaultSeverity.HIGH || severity == FaultSeverity.CRITICAL) {
            node.setStatus(NodeStatus.UNDER_MAINTENANCE);
            gridNodeRepository.save(node);
        }

        // Generate alert
        alertRepository.save(Alert.builder()
                .title("New Fault Reported: " + fault.getTitle())
                .message(String.format("Fault id %d reported on node %s. Severity: %s. Description: %s",
                        fault.getId(), node.getName(), fault.getSeverity().name(), fault.getDescription()))
                .severity(fault.getSeverity().name())
                .status("UNREAD")
                .createdAt(LocalDateTime.now())
                .build());

        return mapToFaultDto(fault);
    }

    @Transactional
    public FaultDto updateFaultStatus(Long id, String statusStr) {
        Fault fault = faultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fault record not found"));

        FaultStatus status;
        try {
            status = FaultStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Must be ACTIVE, RESOLVING, or RESOLVED");
        }

        fault.setStatus(status);

        if (status == FaultStatus.RESOLVED) {
            fault.setResolvedAt(LocalDateTime.now());
            
            // Check if there are other active faults on this node before making it active
            GridNode node = fault.getGridNode();
            boolean hasOtherActiveFaults = node.getFaults().stream()
                    .anyMatch(f -> f.getId() != id && f.getStatus() != FaultStatus.RESOLVED);
            if (!hasOtherActiveFaults) {
                node.setStatus(NodeStatus.ACTIVE);
                gridNodeRepository.save(node);
            }

            alertRepository.save(Alert.builder()
                    .title("Fault Resolved: " + fault.getTitle())
                    .message(String.format("Fault id %d on node %s has been resolved.", fault.getId(), node.getName()))
                    .severity("INFO")
                    .status("UNREAD")
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        fault = faultRepository.save(fault);
        return mapToFaultDto(fault);
    }

    public Page<FaultDto> getFaults(String search, Pageable pageable) {
        Page<Fault> faults;
        if (search != null && !search.trim().isEmpty()) {
            faults = faultRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            faults = faultRepository.findAll(pageable);
        }
        return faults.map(this::mapToFaultDto);
    }

    public List<FaultDto> getActiveFaults() {
        return faultRepository.findByStatus(FaultStatus.ACTIVE).stream()
                .map(this::mapToFaultDto)
                .collect(Collectors.toList());
    }

    // --- Alert Methods ---

    public List<AlertDto> getRecentAlerts() {
        return alertRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(this::mapToAlertDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAlertAsRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        alert.setStatus("READ");
        alertRepository.save(alert);
    }

    @Transactional
    public void dismissAllAlerts() {
        List<Alert> alerts = alertRepository.findByStatusOrderByCreatedAtDesc("UNREAD");
        for (Alert alert : alerts) {
            alert.setStatus("READ");
        }
        alertRepository.saveAll(alerts);
    }

    // --- Mapping Helpers ---

    private FaultDto mapToFaultDto(Fault f) {
        return FaultDto.builder()
                .id(f.getId())
                .gridNodeId(f.getGridNode().getId())
                .gridNodeName(f.getGridNode().getName())
                .title(f.getTitle())
                .description(f.getDescription())
                .severity(f.getSeverity().name())
                .status(f.getStatus().name())
                .reportedAt(f.getReportedAt())
                .resolvedAt(f.getResolvedAt())
                .build();
    }

    private AlertDto mapToAlertDto(Alert a) {
        return AlertDto.builder()
                .id(a.getId())
                .title(a.getTitle())
                .message(a.getMessage())
                .severity(a.getSeverity())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
