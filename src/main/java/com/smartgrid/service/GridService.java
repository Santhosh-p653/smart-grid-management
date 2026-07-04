package com.smartgrid.service;

import com.smartgrid.dto.GridNodeDto;
import com.smartgrid.dto.ZoneDto;
import com.smartgrid.entity.GridNode;
import com.smartgrid.entity.NodeStatus;
import com.smartgrid.entity.PowerReading;
import com.smartgrid.entity.Zone;
import com.smartgrid.exception.BadRequestException;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.GridNodeRepository;
import com.smartgrid.repository.PowerReadingRepository;
import com.smartgrid.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GridService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private PowerReadingRepository powerReadingRepository;

    // --- Zone Methods ---

    @Transactional
    public ZoneDto createZone(ZoneDto dto) {
        if (zoneRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Zone name already exists");
        }
        Zone zone = Zone.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .region(dto.getRegion())
                .build();
        zone = zoneRepository.save(zone);
        return mapToZoneDto(zone);
    }

    @Transactional
    public ZoneDto updateZone(Long id, ZoneDto dto) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (!zone.getName().equalsIgnoreCase(dto.getName()) && zoneRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Zone name already exists");
        }

        zone.setName(dto.getName());
        zone.setDescription(dto.getDescription());
        zone.setRegion(dto.getRegion());
        zone = zoneRepository.save(zone);
        return mapToZoneDto(zone);
    }

    public Page<ZoneDto> getZones(String search, Pageable pageable) {
        Page<Zone> zones;
        if (search != null && !search.trim().isEmpty()) {
            zones = zoneRepository.findByNameContainingIgnoreCaseOrRegionContainingIgnoreCase(search, search, pageable);
        } else {
            zones = zoneRepository.findAll(pageable);
        }
        return zones.map(this::mapToZoneDto);
    }

    public List<ZoneDto> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::mapToZoneDto)
                .collect(Collectors.toList());
    }

    public ZoneDto getZoneById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        return mapToZoneDto(zone);
    }

    @Transactional
    public void deleteZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        zoneRepository.delete(zone);
    }

    // --- Grid Node Methods ---

    @Transactional
    public GridNodeDto createGridNode(GridNodeDto dto) {
        Zone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        NodeStatus status;
        try {
            status = NodeStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            status = NodeStatus.ACTIVE;
        }

        GridNode node = GridNode.builder()
                .name(dto.getName())
                .type(dto.getType())
                .capacity(dto.getCapacity())
                .status(status)
                .zone(zone)
                .build();

        node = gridNodeRepository.save(node);
        return mapToGridNodeDto(node);
    }

    @Transactional
    public GridNodeDto updateGridNode(Long id, GridNodeDto dto) {
        GridNode node = gridNodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));
        Zone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found for Grid Node"));

        NodeStatus status;
        try {
            status = NodeStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            status = node.getStatus();
        }

        node.setName(dto.getName());
        node.setType(dto.getType());
        node.setCapacity(dto.getCapacity());
        node.setStatus(status);
        node.setZone(zone);

        node = gridNodeRepository.save(node);
        return mapToGridNodeDto(node);
    }

    public Page<GridNodeDto> getGridNodes(String search, Pageable pageable) {
        Page<GridNode> nodes;
        if (search != null && !search.trim().isEmpty()) {
            nodes = gridNodeRepository.findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(search, search, pageable);
        } else {
            nodes = gridNodeRepository.findAll(pageable);
        }
        return nodes.map(this::mapToGridNodeDto);
    }

    public List<GridNodeDto> getNodesByZone(Long zoneId) {
        return gridNodeRepository.findByZoneId(zoneId).stream()
                .map(this::mapToGridNodeDto)
                .collect(Collectors.toList());
    }

    public GridNodeDto getGridNodeById(Long id) {
        GridNode node = gridNodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));
        return mapToGridNodeDto(node);
    }

    @Transactional
    public void deleteGridNode(Long id) {
        GridNode node = gridNodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));
        gridNodeRepository.delete(node);
    }

    // --- Mapping Helpers ---

    public ZoneDto mapToZoneDto(Zone zone) {
        return ZoneDto.builder()
                .id(zone.getId())
                .name(zone.getName())
                .description(zone.getDescription())
                .region(zone.getRegion())
                .nodeCount(zone.getGridNodes() != null ? zone.getGridNodes().size() : 0)
                .build();
    }

    public GridNodeDto mapToGridNodeDto(GridNode node) {
        GridNodeDto.GridNodeDtoBuilder builder = GridNodeDto.builder()
                .id(node.getId())
                .name(node.getName())
                .type(node.getType())
                .capacity(node.getCapacity())
                .status(node.getStatus().name())
                .zoneId(node.getZone().getId())
                .zoneName(node.getZone().getName());

        // Attach live telemetry if available
        powerReadingRepository.findFirstByGridNodeIdOrderByTimestampDesc(node.getId())
                .ifPresentOrElse(reading -> {
                    builder.currentVoltage(reading.getVoltage());
                    builder.currentLoad(reading.getActiveLoad());
                    builder.currentHealth(reading.getHealthStatus());
                }, () -> {
                    builder.currentVoltage(230.0); // simulated default
                    builder.currentLoad(0.0);
                    builder.currentHealth("NORMAL");
                });

        return builder.build();
    }
}
