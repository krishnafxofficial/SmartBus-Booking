package com.busbooking.system.repository;

import com.busbooking.system.entity.BookingSeat;
import com.busbooking.system.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    @Query("""
        SELECT bs
        FROM BookingSeat bs
        WHERE bs.seat.id IN :seatIds
          AND bs.booking.schedule.id = :scheduleId
          AND bs.booking.status = :status
    """)
    List<BookingSeat> findBookedSeats(
            @Param("seatIds") List<Long> seatIds,
            @Param("scheduleId") Long scheduleId,
            @Param("status") BookingStatus status
    );
   
    List<BookingSeat> findByBookingScheduleIdAndBookingStatus(
            Long scheduleId,
            BookingStatus status
    );
}