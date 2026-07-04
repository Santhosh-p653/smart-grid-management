package com.smartgrid.repository;

import com.smartgrid.entity.Fault;
import com.smartgrid.entity.FaultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaultRepository extends JpaRepository<Fault, Long> {
    List<Fault> findByStatus(FaultStatus status);
    long countByStatus(FaultStatus status);
    Page<Fault> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
