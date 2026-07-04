package com.smartgrid.repository;

import com.smartgrid.entity.PowerReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PowerReadingRepository extends JpaRepository<PowerReading, Long> {
    Optional<PowerReading> findFirstByGridNodeIdOrderByTimestampDesc(Long gridNodeId);
    List<PowerReading> findByGridNodeIdOrderByTimestampDesc(Long gridNodeId, Pageable pageable);
}
