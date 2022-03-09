package com.example.elevatorsystem.services;

import com.example.elevatorsystem.entities.Elevator;
import com.example.elevatorsystem.repositories.ElevatorRepo;
import com.example.elevatorsystem.util.ElevatorEngine;
import com.example.elevatorsystem.util.classes.Order;
import com.example.elevatorsystem.util.classes.OrderInElevator;
import com.example.elevatorsystem.util.exceptions.ElevatorNotFoundException;
import com.example.elevatorsystem.util.classes.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ElevatorService {
    private final ElevatorRepo elevatorRepo;
    private final ElevatorEngine elevatorEngine;

    public Elevator saveElevator(Elevator elevator){
        log.info("Saving new elevator with id {} to database", elevator.getId());
        return elevatorRepo.save(elevator);
    }

    public List<Elevator> getElevators(){
        log.info("Fetching all elevators");
        return elevatorRepo.findAll();
    }


    public Elevator updateElevator(Status status) throws ElevatorNotFoundException {
        Optional<Elevator> elevatorOptional = elevatorRepo.findElevatorById(status.getId());
        if(elevatorOptional.isPresent()){
            log.info("Updating elevator with id: {}", status.getId());
            Elevator elevator = elevatorOptional.get();
            elevator.updateElevator(status.getCurrentFloor(), status.getDestinationFloor(),
                    new ArrayList<>(), new ArrayList<>());
            return elevatorRepo.save(elevator);
        } else{
            log.error("Elevator with given id doesn't exist!");
            throw new ElevatorNotFoundException("Elevator with given id doesn't exist!");
        }
    }

    public List<Elevator> step(){
        log.info("Executing one step of the elevator simulation...");
        List<Elevator> elevators = elevatorRepo.findAll();
        elevatorEngine.step(elevators);
        return elevatorRepo.saveAll(elevators);
    }

    public List<Elevator> orderElevator(Order order){
        log.info("Ordering elevator on {} floor with {} direction", order.getFloor(), order.getDirection());
        List<Elevator> elevators = elevatorRepo.findAll();
        int direction;
        if(order.getDirection().equals("Up")){
            direction = 1;
        } else {
            direction = -1;
        }
        elevatorEngine.orderElevator(order.getFloor(), direction, elevators);
        return elevators;
    }

    public Elevator orderFloorInElevator(OrderInElevator order) throws ElevatorNotFoundException {
        log.info("Ordering floor {} in {} elevator", order.getFloor(), order.getId());
        Optional<Elevator> elevatorOptional = elevatorRepo.findElevatorById(order.getId());
        if(elevatorOptional.isPresent()){
            Elevator elevator = elevatorOptional.get();
            elevator.getOrderedFloors().add(order.getFloor());
            return elevatorRepo.save(elevator);
        } else{
            log.error("Elevator with given id doesn't exist!");
            throw new ElevatorNotFoundException("Elevator with given id doesn't exist!");
        }
    }

    public Long deleteElevator(Long id) throws ElevatorNotFoundException {
        log.info("Deleting elevator with {} id", id);
        Optional<Elevator> elevatorOptional = elevatorRepo.findElevatorById(id);
        if(elevatorOptional.isPresent()){
            Elevator elevator = elevatorOptional.get();
            elevatorRepo.delete(elevator);
            return id;
        } else{
            log.error("Elevator with given id doesn't exist!");
            throw new ElevatorNotFoundException("Elevator with given id doesn't exist!");
        }
    }
}
