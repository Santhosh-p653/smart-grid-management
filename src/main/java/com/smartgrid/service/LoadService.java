package com.smartgrid.service;

import com.smartgrid.dto.GridNodeDto;
import com.smartgrid.entity.GridNode;
import com.smartgrid.entity.PowerReading;
import com.smartgrid.repository.GridNodeRepository;
import com.smartgrid.repository.PowerReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoadService {

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private PowerReadingRepository powerReadingRepository;

    public List<Map<String, Object>> analyzeLoad() {
        List<GridNode> nodes = gridNodeRepository.findAll();
        List<Map<String, Object>> analysis = new ArrayList<>();

        for (GridNode node : nodes) {
            Map<String, Object> result = new HashMap<>();
            result.put("nodeId", node.getId());
            result.put("nodeName", node.getName());
            result.put("capacity", node.getCapacity()); // MW

            double currentLoad = 0.0;
            double voltage = 230.0;
            
            // Get latest reading
            PowerReading reading = powerReadingRepository.findFirstByGridNodeIdOrderByTimestampDesc(node.getId()).orElse(null);
            if (reading != null) {
                currentLoad = reading.getActiveLoad();
                voltage = reading.getVoltage();
            }

            result.put("currentLoad", currentLoad);
            result.put("voltage", voltage);
            
            double loadRatio = currentLoad / node.getCapacity();
            result.put("loadRatio", loadRatio * 100); // percentage

            String loadStatus;
            String recommendation = "System normal. Maintain current operations.";

            if (loadRatio >= 1.0) {
                loadStatus = "OVERLOADED";
                recommendation = "CRITICAL: Shed load immediately! Shift 20% to adjacent transformer nodes.";
            } else if (loadRatio >= 0.85) {
                loadStatus = "NEAR_OVERLOAD";
                recommendation = "WARNING: Load balancing recommended. Shift 10% load to adjacent substations.";
            } else {
                loadStatus = "STABLE";
            }

            result.put("status", loadStatus);
            result.put("recommendation", recommendation);
            analysis.add(result);
        }

        return analysis;
    }
}
