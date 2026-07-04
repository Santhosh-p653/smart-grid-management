package com.smartgrid.controller;

import com.smartgrid.dto.GridNodeDto;
import com.smartgrid.dto.ZoneDto;
import com.smartgrid.service.GridService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grid")
public class GridController {

    @Autowired
    private GridService gridService;

    // --- Zone Endpoints ---

    @PostMapping("/zones")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ZoneDto> createZone(@Valid @RequestBody ZoneDto dto) {
        return ResponseEntity.ok(gridService.createZone(dto));
    }

    @PutMapping("/zones/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ZoneDto> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneDto dto) {
        return ResponseEntity.ok(gridService.updateZone(id, dto));
    }

    @GetMapping("/zones")
    public ResponseEntity<Page<ZoneDto>> getZones(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(gridService.getZones(search, pageable));
    }

    @GetMapping("/zones/all")
    public ResponseEntity<List<ZoneDto>> getAllZones() {
        return ResponseEntity.ok(gridService.getAllZones());
    }

    @GetMapping("/zones/{id}")
    public ResponseEntity<ZoneDto> getZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(gridService.getZoneById(id));
    }

    @DeleteMapping("/zones/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteZone(@PathVariable Long id) {
        gridService.deleteZone(id);
        return ResponseEntity.ok("Zone deleted successfully");
    }

    // --- Grid Node Endpoints ---

    @PostMapping("/nodes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GridNodeDto> createGridNode(@Valid @RequestBody GridNodeDto dto) {
        return ResponseEntity.ok(gridService.createGridNode(dto));
    }

    @PutMapping("/nodes/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GridNodeDto> updateGridNode(@PathVariable Long id, @Valid @RequestBody GridNodeDto dto) {
        return ResponseEntity.ok(gridService.updateGridNode(id, dto));
    }

    @GetMapping("/nodes")
    public ResponseEntity<Page<GridNodeDto>> getGridNodes(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(gridService.getGridNodes(search, pageable));
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<GridNodeDto> getGridNodeById(@PathVariable Long id) {
        return ResponseEntity.ok(gridService.getGridNodeById(id));
    }

    @GetMapping("/zones/{zoneId}/nodes")
    public ResponseEntity<List<GridNodeDto>> getNodesByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(gridService.getNodesByZone(zoneId));
    }

    @DeleteMapping("/nodes/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteGridNode(@PathVariable Long id) {
        gridService.deleteGridNode(id);
        return ResponseEntity.ok("Grid Node deleted successfully");
    }
}
