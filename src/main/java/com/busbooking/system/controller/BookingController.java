package com.busbooking.system.controller;

import com.busbooking.system.dto.BookingRequest;
import com.busbooking.system.entity.Booking;
import com.busbooking.system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.busbooking.system.service.RedisSeatLockService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;
private final RedisSeatLockService redisSeatLockService;

    @PostMapping
    public Booking createBooking(
            @RequestBody BookingRequest request
    ) {
        return service.createBooking(request);
    }

    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings() {
        return service.getMyBookings();
    }
    
    @PutMapping("/{bookingId}/cancel")
    public Booking cancelBooking(
            @PathVariable Long bookingId
    ) {
        return service.cancelBooking(bookingId);
    }

}