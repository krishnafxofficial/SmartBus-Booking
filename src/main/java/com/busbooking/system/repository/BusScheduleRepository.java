package com.busbooking.system.repository;

import com.busbooking.system.entity.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {

    @Query("""
        SELECT bs
        FROM BusSchedule bs
        WHERE bs.route.source = :source
          AND bs.route.destination = :destination
          AND bs.departureTime BETWEEN :startOfDay AND :endOfDay
    """)
    List<BusSchedule> searchSchedules(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}