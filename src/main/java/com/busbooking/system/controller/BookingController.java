package com.busbooking.system.controller;

import com.busbooking.system.dto.BookingRequest;
import com.busbooking.system.entity.Booking;
import com.busbooking.system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public Booking createBooking(
            @RequestBody BookingRequest request
    ) {
        return service.createBooking(request);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(
            @PathVariable Long userId
    ) {
        return service.getBookingsByUser(userId);
    }
    @PutMapping("/{bookingId}/cancel")
    public Booking cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId
    ) {
        return service.cancelBooking(
                bookingId,
                userId
        );
    }
}