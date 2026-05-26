package com.busbooking.system.controller;

import com.busbooking.system.entity.Bus;
import com.busbooking.system.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bus create(@RequestBody Bus bus) {
        return busService.create(bus);
    }

    @GetMapping
    public List<Bus> findAll() {
        return busService.findAll();
    }

    @GetMapping("/{id}")
    public Bus findById(@PathVariable Long id) {
        return busService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        busService.deleteById(id);
    }
}
