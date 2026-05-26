package com.busbooking.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.busbooking.system.entity.BusSchedule;
import com.busbooking.system.service.BusScheduleService;
import com.busbooking.system.dto.BusScheduleRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedules")
public class BusScheduleController {

    @Autowired
    private BusScheduleService service;

    @PostMapping
    public BusSchedule create(@RequestBody BusScheduleRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<BusSchedule> getAll() {
        return service.getAll();
    }
    @GetMapping("/search")
public List<BusSchedule> search(
        @RequestParam String source,
        @RequestParam String destination,
        @RequestParam LocalDate date
) {
    return service.search(source, destination, date);
}

    @GetMapping("/{id}")
    public BusSchedule getById(@PathVariable Long id) {
        return service.getById(id);
    }
}