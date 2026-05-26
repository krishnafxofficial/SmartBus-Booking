package com.busbooking.system.controller;

import com.busbooking.system.dto.SeatAvailabilityResponse;
import com.busbooking.system.dto.SeatRequest;
import com.busbooking.system.entity.Seat;
import com.busbooking.system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService service;

    @PostMapping
    public Seat create(
            @RequestBody SeatRequest request
    ) {
        return service.create(request);
    }

    @GetMapping("/bus/{busId}")
    public List<Seat> getSeatsByBus(
            @PathVariable Long busId
    ) {
        return service.getSeatsByBus(busId);
    }

    @GetMapping("/available/{scheduleId}")
    public List<SeatAvailabilityResponse> getAvailableSeats(
            @PathVariable Long scheduleId
    ) {
        return service.getAvailableSeats(scheduleId);
    }
}