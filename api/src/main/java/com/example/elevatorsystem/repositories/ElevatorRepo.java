package com.example.elevatorsystem.repositories;

import com.example.elevatorsystem.entities.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElevatorRepo extends JpaRepository<Elevator, Long> {
    Optional<Elevator> findElevatorById(Long id);
}
