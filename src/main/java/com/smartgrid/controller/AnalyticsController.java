package com.smartgrid.controller;

import com.smartgrid.dto.DashboardStatsDto;
import com.smartgrid.dto.ReportDto;
import com.smartgrid.security.CustomUserDetails;
import com.smartgrid.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }

    @PostMapping("/reports/generate")
    public ResponseEntity<ReportDto> generateReport(
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(analyticsService.generateReport(type, userDetails.getUsername()));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getReports() {
        return ResponseEntity.ok(analyticsService.getReports());
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getReportById(id));
    }
}
