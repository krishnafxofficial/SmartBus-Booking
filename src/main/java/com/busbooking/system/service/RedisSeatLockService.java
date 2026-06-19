package com.busbooking.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisSeatLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean lockSeat(
            Long scheduleId,
            Long seatId,
            Long userId
    ) {

        String key =
                "seat:" + scheduleId + ":" + seatId;

        Boolean success =
                redisTemplate.opsForValue()
                        .setIfAbsent(
                                key,
                                userId.toString(),
                                Duration.ofMinutes(5)
                        );

        return Boolean.TRUE.equals(success);
    }

    public void releaseSeat(
            Long scheduleId,
            Long seatId
    ) {

        String key =
                "seat:" + scheduleId + ":" + seatId;

        redisTemplate.delete(key);
    }

    public Long getLockedUser(
            Long scheduleId,
            Long seatId
    ) {

        String key =
                "seat:" + scheduleId + ":" + seatId;

        String value =
                redisTemplate.opsForValue()
                        .get(key);

        return value == null
                ? null
                : Long.valueOf(value);
    }
}