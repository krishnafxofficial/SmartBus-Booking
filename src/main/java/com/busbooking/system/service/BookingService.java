package com.busbooking.system.service;

import com.busbooking.system.dto.BookingRequest;
import com.busbooking.system.entity.*;
import com.busbooking.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final BookingSeatRepository bookingSeatRepo;
    private final UserRepository userRepo;
    private final BusScheduleRepository scheduleRepo;
    private final SeatRepository seatRepo;

    @Transactional
    public Booking createBooking(BookingRequest request) {

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BusSchedule schedule = scheduleRepo
                .findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // LOCK seats
        List<Seat> seats = seatRepo.findSeatsWithLock(
                request.getSeatIds());

        // validate seat ids
        if (seats.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Invalid seat ids");
        }

        // duplicate booking check
        List<BookingSeat> bookedSeats = bookingSeatRepo.findBookedSeats(
                request.getSeatIds(),
                request.getScheduleId(),
                BookingStatus.CONFIRMED);

        if (!bookedSeats.isEmpty()) {
            throw new RuntimeException(
                    "One or more seats already booked");
        }

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)
                .status(BookingStatus.CONFIRMED)
                .build();

        booking = bookingRepo.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Seat seat : seats) {

            if (!seat.getBus().getId()
                    .equals(schedule.getBus().getId())) {

                throw new RuntimeException(
                        "Seat does not belong to this bus");
            }

            bookingSeats.add(
                    BookingSeat.builder()
                            .booking(booking)
                            .seat(seat)
                            .build());
        }

        bookingSeatRepo.saveAll(bookingSeats);

        return booking;
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepo.findByUserId(userId);
    }

    public Booking cancelBooking(
            Long bookingId,
            Long userId) {

        Booking booking = bookingRepo
                .findByIdAndUser_Id(
                        bookingId,
                        userId);

        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepo.save(booking);
    }
}