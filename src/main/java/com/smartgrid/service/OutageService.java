package com.smartgrid.service;

import com.smartgrid.dto.OutageDto;
import com.smartgrid.entity.GridNode;
import com.smartgrid.entity.NodeStatus;
import com.smartgrid.entity.Outage;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.GridNodeRepository;
import com.smartgrid.repository.OutageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutageService {

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Transactional
    public OutageDto reportOutage(OutageDto dto) {
        GridNode node = gridNodeRepository.findById(dto.getGridNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Grid Node not found"));

        Outage outage = Outage.builder()
                .gridNode(node)
                .startTime(LocalDateTime.now())
                .status("ACTIVE")
                .description(dto.getDescription())
                .build();

        outage = outageRepository.save(outage);

        // Turn node inactive due to blackout
        node.setStatus(NodeStatus.INACTIVE);
        gridNodeRepository.save(node);

        return mapToDto(outage);
    }

    @Transactional
    public OutageDto restorePower(Long id) {
        Outage outage = outageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Outage record not found"));

        outage.setStatus("RESTORED");
        outage.setEndTime(LocalDateTime.now());

        // Restore node to active
        GridNode node = outage.getGridNode();
        node.setStatus(NodeStatus.ACTIVE);
        gridNodeRepository.save(node);

        outage = outageRepository.save(outage);
        return mapToDto(outage);
    }

    public List<OutageDto> getAllOutages() {
        return outageRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OutageDto> getActiveOutages() {
        return outageRepository.findByStatus("ACTIVE").stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OutageDto> getOutageHistory(Long nodeId) {
        return outageRepository.findByGridNodeId(nodeId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private OutageDto mapToDto(Outage o) {
        return OutageDto.builder()
                .id(o.getId())
                .gridNodeId(o.getGridNode().getId())
                .gridNodeName(o.getGridNode().getName())
                .startTime(o.getStartTime())
                .endTime(o.getEndTime())
                .status(o.getStatus())
                .description(o.getDescription())
                .build();
    }
}
