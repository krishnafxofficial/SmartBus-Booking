package com.busbooking.system.service;

import com.busbooking.system.dto.SeatAvailabilityResponse;
import com.busbooking.system.dto.SeatRequest;
import com.busbooking.system.entity.BookingSeat;
import com.busbooking.system.entity.Bus;
import com.busbooking.system.entity.BusSchedule;
import com.busbooking.system.entity.Seat;
import com.busbooking.system.repository.BookingSeatRepository;
import com.busbooking.system.repository.BusRepository;
import com.busbooking.system.repository.BusScheduleRepository;
import com.busbooking.system.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.busbooking.system.entity.BookingStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepo;
    private final BusRepository busRepo;

    // Add 2nd dependencies 
    private final BusScheduleRepository scheduleRepo;
    private final BookingSeatRepository bookingSeatRepo;

    public Seat create(SeatRequest request) {

        Bus bus = busRepo.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Seat seat = Seat.builder()
                .bus(bus)
                .seatNumber(request.getSeatNumber())
                .build();

        return seatRepo.save(seat);
    }

    public List<Seat> getSeatsByBus(Long busId) {
        return seatRepo.findByBusId(busId);
    }

    
    public List<SeatAvailabilityResponse> getAvailableSeats(Long scheduleId) {

        // schedule fetch
        BusSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // all seats of this bus
        List<Seat> allSeats = seatRepo.findByBusId(schedule.getBus().getId());

        // booked seats for this schedule
        List<BookingSeat> bookedSeats =
        bookingSeatRepo
        .findByBookingScheduleIdAndBookingStatus(
                scheduleId,
                BookingStatus.CONFIRMED
        );

        // booked seat ids
        Set<Long> bookedSeatIds = bookedSeats.stream()
                .map(bs -> bs.getSeat().getId())
                .collect(Collectors.toSet());

        // response prepare
        return allSeats.stream()
                .map(seat -> new SeatAvailabilityResponse(
                        seat.getId(),
                        seat.getSeatNumber(),
                        bookedSeatIds.contains(seat.getId())
                ))
                .toList();
    }
}