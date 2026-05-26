package com.busbooking.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequest {

    private Long userId;
    private Long scheduleId;
    private List<Long> seatIds;
}