package com.smartgrid.repository;

import com.smartgrid.entity.Outage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OutageRepository extends JpaRepository<Outage, Long> {
    List<Outage> findByStatus(String status);
    long countByStatus(String status);
    List<Outage> findByGridNodeId(Long gridNodeId);
}
