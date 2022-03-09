package com.example.elevatorsystem.controllers;

import com.example.elevatorsystem.entities.Elevator;
import com.example.elevatorsystem.services.ElevatorService;
import com.example.elevatorsystem.util.classes.Order;
import com.example.elevatorsystem.util.classes.OrderInElevator;
import com.example.elevatorsystem.util.classes.Status;
import com.example.elevatorsystem.util.exceptions.ElevatorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ElevatorController {
    private final ElevatorService elevatorService;

    @GetMapping("/elevators")
    public ResponseEntity<List<Elevator>> getElevators() {
        return ResponseEntity.ok().body(elevatorService.getElevators());
    }

    @PostMapping("/elevator")
    public ResponseEntity<Elevator> saveElevator(@RequestBody Elevator elevator){
        return ResponseEntity.ok().body(elevatorService.saveElevator(elevator));
    }

    @PutMapping("/elevator")
    public ResponseEntity<Elevator> updateElevator(@RequestBody Status status) throws ElevatorNotFoundException {
        return ResponseEntity.ok().body(elevatorService.updateElevator(status));
    }

    @GetMapping("/step")
    public ResponseEntity<List<Elevator>> updateElevator(){
        return ResponseEntity.ok().body(elevatorService.step());
    }

    @PostMapping("/order")
    public ResponseEntity<List<Elevator>> orderElevator(@RequestBody Order order){
        return ResponseEntity.ok().body(elevatorService.orderElevator(order));
    }

    @PostMapping("/order/floor")
    public ResponseEntity<Elevator> orderFloor(@RequestBody OrderInElevator order) throws ElevatorNotFoundException {
        return ResponseEntity.ok().body(elevatorService.orderFloorInElevator(order));
    }

    @DeleteMapping("/elevator")
    public ResponseEntity<Long> deleteElevator(@RequestParam Long id) throws ElevatorNotFoundException {
        return ResponseEntity.ok().body(elevatorService.deleteElevator(id));
    }
}

