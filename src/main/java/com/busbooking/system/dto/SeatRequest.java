package com.busbooking.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatRequest {

    private Long busId;
    private String seatNumber;
}