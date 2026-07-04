package com.smartgrid.repository;

import com.smartgrid.entity.GridNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GridNodeRepository extends JpaRepository<GridNode, Long> {
    List<GridNode> findByZoneId(Long zoneId);
    Page<GridNode> findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(String name, String type, Pageable pageable);
}
