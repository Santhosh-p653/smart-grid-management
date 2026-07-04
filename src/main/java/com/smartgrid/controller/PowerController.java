package com.smartgrid.controller;

import com.smartgrid.dto.PowerReadingDto;
import com.smartgrid.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/power")
public class PowerController {

    @Autowired
    private MonitoringService monitoringService;

    @PostMapping("/readings")
    public ResponseEntity<PowerReadingDto> recordReading(@Valid @RequestBody PowerReadingDto dto) {
        return ResponseEntity.ok(monitoringService.recordReading(dto));
    }

    @GetMapping("/readings/{nodeId}")
    public ResponseEntity<List<PowerReadingDto>> getReadingsHistory(
            @PathVariable Long nodeId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(monitoringService.getReadingsHistory(nodeId, limit));
    }
}
