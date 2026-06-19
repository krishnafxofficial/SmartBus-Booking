package com.busbooking.system.controller;

import com.busbooking.system.dto.SeatLockRequest;
import com.busbooking.system.entity.User;
import com.busbooking.system.service.CurrentUserService;
import com.busbooking.system.service.RedisSeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seat-lock")
public class SeatLockController {

    private final RedisSeatLockService redisSeatLockService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public String lockSeat(
            @RequestBody SeatLockRequest request
    ) {

        User user =
                currentUserService.getCurrentUser();

        boolean locked =
                redisSeatLockService.lockSeat(
                        request.getScheduleId(),
                        request.getSeatId(),
                        user.getId()
                );

        return locked
                ? "Seat Locked"
                : "Already Locked";
    }
}