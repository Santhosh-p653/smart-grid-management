package com.smartgrid.repository;

import com.smartgrid.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findByConsumerIdOrderByReadingDateDesc(Long consumerId);

    @Query("SELECT COALESCE(SUM(mr.activePower), 0) FROM MeterReading mr")
    Double sumAllActivePower();
}
