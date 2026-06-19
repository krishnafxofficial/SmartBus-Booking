package com.busbooking.system.service;

import com.busbooking.system.dto.BookingRequest;
import com.busbooking.system.entity.*;
import com.busbooking.system.exception.BookingNotFoundException;
import com.busbooking.system.exception.InvalidSeatException;
import com.busbooking.system.exception.SeatAlreadyBookedException;
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
        private final BusScheduleRepository scheduleRepo;
        private final SeatRepository seatRepo;
        private final RedisSeatLockService redisSeatLockService;
        private final CurrentUserService currentUserService;

        @Transactional
        public Booking createBooking(BookingRequest request) {

                User user = currentUserService.getCurrentUser();
    

                for (Long seatId : request.getSeatIds()) {

                        Long lockedUserId = redisSeatLockService.getLockedUser(
                                        request.getScheduleId(),
                                        seatId);

                        if (lockedUserId == null) {

                                throw new RuntimeException(
                                                "Please lock seat before booking");
                        }

                        if (!lockedUserId.equals(
                                        user.getId())) {

                                throw new RuntimeException(
                                                "Seat is currently locked by another user");
                        }
                }

                // duplicate seat ids validation
                if (request.getSeatIds().size() != request.getSeatIds()
                                .stream()
                                .distinct()
                                .count()) {

                        throw new InvalidSeatException(
                                        "Duplicate seat ids are not allowed");
                }

                BusSchedule schedule = scheduleRepo
                                .findById(request.getScheduleId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Schedule not found"));

                // LOCK seats
                List<Seat> seats = seatRepo.findSeatsWithLock(
                                request.getSeatIds());

                // validate seat ids
                if (seats.size() != request.getSeatIds().size()) {

                        throw new InvalidSeatException(
                                        "Invalid seat ids");
                }

                // duplicate booking check
                List<BookingSeat> bookedSeats = bookingSeatRepo.findBookedSeats(
                                request.getSeatIds(),
                                request.getScheduleId(),
                                BookingStatus.CONFIRMED);

                if (!bookedSeats.isEmpty()) {

                        throw new SeatAlreadyBookedException(
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

                                throw new InvalidSeatException(
                                                "Seat does not belong to this bus");
                        }

                        bookingSeats.add(
                                        BookingSeat.builder()
                                                        .booking(booking)
                                                        .seat(seat)
                                                        .build());
                }

                bookingSeatRepo.saveAll(bookingSeats);

                for (Long seatId : request.getSeatIds()) {

                        redisSeatLockService.releaseSeat(
                                request.getScheduleId(),
                                seatId
                        );
                    }

                return booking;
        }

        public List<Booking> getMyBookings() {

                User user = currentUserService.getCurrentUser();

                return bookingRepo.findByUserId(
                        user.getId()
                );
        }

        public Booking cancelBooking(
                        Long bookingId) {

                User user = currentUserService.getCurrentUser();
                  

                Booking booking = bookingRepo
                                .findByIdAndUser_Id(
                                                bookingId,
                                                user.getId());

                if (booking == null) {
                        throw new BookingNotFoundException(
                                        "Booking not found");
                }

                booking.setStatus(
                                BookingStatus.CANCELLED);

                return bookingRepo.save(booking);
        }
}