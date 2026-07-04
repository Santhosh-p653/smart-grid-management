package com.smartgrid.repository;

import com.smartgrid.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByName(String name);
    Page<Zone> findByNameContainingIgnoreCaseOrRegionContainingIgnoreCase(String name, String region, Pageable pageable);
}
