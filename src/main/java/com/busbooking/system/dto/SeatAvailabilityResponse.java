package com.busbooking.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatAvailabilityResponse {

    private Long seatId;
    private String seatNumber;
    private boolean booked;
}