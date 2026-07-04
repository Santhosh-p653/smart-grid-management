package com.smartgrid.repository;

import com.smartgrid.entity.Report;
import com.smartgrid.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByTypeOrderByGeneratedAtDesc(ReportType type);
}
