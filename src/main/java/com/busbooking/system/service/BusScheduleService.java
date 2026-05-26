package com.busbooking.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.busbooking.system.entity.Bus;
import com.busbooking.system.entity.Route;
import com.busbooking.system.entity.BusSchedule;
import com.busbooking.system.repository.BusRepository;
import com.busbooking.system.repository.RouteRepository;
import com.busbooking.system.repository.BusScheduleRepository;
import com.busbooking.system.dto.BusScheduleRequest;

@Service
public class BusScheduleService {

    @Autowired
    private BusScheduleRepository repo;

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private RouteRepository routeRepo;

    // CREATE SCHEDULE
    public BusSchedule save(BusScheduleRequest request) {

        Bus bus = busRepo.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Route route = routeRepo.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        BusSchedule schedule = BusSchedule.builder()
                .bus(bus)
                .route(route)
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .build();

        return repo.save(schedule);
    }

    // GET ALL
    public List<BusSchedule> getAll() {
        return repo.findAll();
    }

    // GET BY ID
    public BusSchedule getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // SEARCH API
    public List<BusSchedule> search(String source, String destination, LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
    
        return repo.searchSchedules(
                source,
                destination,
                startOfDay,
                endOfDay
        );
    }
}