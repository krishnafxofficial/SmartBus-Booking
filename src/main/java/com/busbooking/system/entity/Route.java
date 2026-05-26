package com.busbooking.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "source", nullable = false, length = 150)
    private String source;

    @Column(name = "destination", nullable = false, length = 150)
    private String destination;

    // IMPORTANT: Prevent infinite JSON recursion
    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BusSchedule> schedules;
}