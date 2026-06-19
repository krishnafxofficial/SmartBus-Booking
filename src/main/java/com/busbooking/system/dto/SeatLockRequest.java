package com.busbooking.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatLockRequest {

    private Long scheduleId;
    private Long seatId;
}