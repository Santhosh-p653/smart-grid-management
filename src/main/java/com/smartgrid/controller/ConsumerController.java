package com.smartgrid.controller;

import com.smartgrid.dto.ConsumerDto;
import com.smartgrid.dto.MeterReadingDto;
import com.smartgrid.service.ConsumptionService;
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
@RequestMapping("/api/consumers")
public class ConsumerController {

    @Autowired
    private ConsumptionService consumptionService;

    @PostMapping
    public ResponseEntity<ConsumerDto> createConsumer(@Valid @RequestBody ConsumerDto dto) {
        return ResponseEntity.ok(consumptionService.createConsumer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumerDto> updateConsumer(@PathVariable Long id, @Valid @RequestBody ConsumerDto dto) {
        return ResponseEntity.ok(consumptionService.updateConsumer(id, dto));
    }

    @GetMapping
    public ResponseEntity<Page<ConsumerDto>> getConsumers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(consumptionService.getConsumers(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumerDto> getConsumerById(@PathVariable Long id) {
        return ResponseEntity.ok(consumptionService.getConsumerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConsumer(@PathVariable Long id) {
        consumptionService.deleteConsumer(id);
        return ResponseEntity.ok("Consumer deleted successfully");
    }

    // --- Meter Readings ---

    @PostMapping("/{id}/readings")
    public ResponseEntity<MeterReadingDto> addMeterReading(
            @PathVariable Long id,
            @Valid @RequestBody MeterReadingDto dto) {
        dto.setConsumerId(id);
        return ResponseEntity.ok(consumptionService.addMeterReading(dto));
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<List<MeterReadingDto>> getConsumerReadings(@PathVariable Long id) {
        return ResponseEntity.ok(consumptionService.getConsumerReadings(id));
    }

    @GetMapping("/readings/all")
    public ResponseEntity<List<MeterReadingDto>> getAllReadings() {
        return ResponseEntity.ok(consumptionService.getAllReadings());
    }

    @PutMapping("/readings/{readingId}/pay")
    public ResponseEntity<?> payBill(@PathVariable Long readingId) {
        consumptionService.payBill(readingId);
        return ResponseEntity.ok("Bill paid successfully");
    }
}
