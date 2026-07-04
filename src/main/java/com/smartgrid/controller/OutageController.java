package com.smartgrid.controller;

import com.smartgrid.dto.OutageDto;
import com.smartgrid.service.OutageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/outages")
public class OutageController {

    @Autowired
    private OutageService outageService;

    @PostMapping
    public ResponseEntity<OutageDto> reportOutage(@Valid @RequestBody OutageDto dto) {
        return ResponseEntity.ok(outageService.reportOutage(dto));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<OutageDto> restorePower(@PathVariable Long id) {
        return ResponseEntity.ok(outageService.restorePower(id));
    }

    @GetMapping
    public ResponseEntity<List<OutageDto>> getAllOutages() {
        return ResponseEntity.ok(outageService.getAllOutages());
    }

    @GetMapping("/active")
    public ResponseEntity<List<OutageDto>> getActiveOutages() {
        return ResponseEntity.ok(outageService.getActiveOutages());
    }

    @GetMapping("/history/{nodeId}")
    public ResponseEntity<List<OutageDto>> getOutageHistory(@PathVariable Long nodeId) {
        return ResponseEntity.ok(outageService.getOutageHistory(nodeId));
    }
}
