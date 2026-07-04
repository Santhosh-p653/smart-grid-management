package com.smartgrid.controller;

import com.smartgrid.dto.AlertDto;
import com.smartgrid.dto.FaultDto;
import com.smartgrid.service.FaultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faults")
public class FaultController {

    @Autowired
    private FaultService faultService;

    @PostMapping
    public ResponseEntity<FaultDto> reportFault(@Valid @RequestBody FaultDto dto) {
        return ResponseEntity.ok(faultService.reportFault(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FaultDto> updateFaultStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(faultService.updateFaultStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<Page<FaultDto>> getFaults(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(faultService.getFaults(search, pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<FaultDto>> getActiveFaults() {
        return ResponseEntity.ok(faultService.getActiveFaults());
    }

    // --- System Alerts ---

    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDto>> getRecentAlerts() {
        return ResponseEntity.ok(faultService.getRecentAlerts());
    }

    @PutMapping("/alerts/{alertId}/read")
    public ResponseEntity<?> markAlertAsRead(@PathVariable Long alertId) {
        faultService.markAlertAsRead(alertId);
        return ResponseEntity.ok("Alert marked as read");
    }

    @PutMapping("/alerts/read-all")
    public ResponseEntity<?> markAllAlertsAsRead() {
        faultService.dismissAllAlerts();
        return ResponseEntity.ok("All alerts marked as read");
    }
}
