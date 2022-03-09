package com.example.elevatorsystem.util;

import com.example.elevatorsystem.entities.Elevator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@AllArgsConstructor
public class ElevatorEngine {
    private final Queue<Integer> waitingFloors = new LinkedList<>();

    public void orderElevator(int floor, int direction, List<Elevator> elevators){
        if(!isAnyElevatorFree(elevators)){
            Elevator chosenElevator = null;
            int distance = Integer.MAX_VALUE;
            for(Elevator elevator: elevators){
                if(checkIfElevatorCanStopOnFloor(floor, elevator, direction)){
                    if(Math.abs(elevator.getCurrentFloor() - floor) < distance){
                        chosenElevator = elevator;
                        distance = Math.abs(elevator.getCurrentFloor() - floor);
                    }
                }
            }
            if(chosenElevator != null) {
                chosenElevator.getFloors().add(floor);
                return;
            }
        }
        if(!waitingFloors.contains(floor)
                && !isElevatorForFloorOrdered(floor, elevators)){
            waitingFloors.add(floor);
        }
    }


    public void step(List<Elevator> elevators){
        updateFloorsForOrderedFloors(elevators);
        updateDestinationFloorsForWaitingFloors(elevators);
        updateElevatorActualFloors(elevators);
    }


    private void updateFloorsForOrderedFloors(List<Elevator> elevators){
        for(Elevator elevator: elevators){
            if(elevator.getCurrentFloor() == elevator.getDestinationFloor()){
                List<Integer> orderedFloors = elevator.getOrderedFloors();
                if(orderedFloors.size() > 0){
                    int floor = orderedFloors.get(0);
                    elevator.setDestinationFloor(floor);
                    elevator.getFloors().add(floor);
                    orderedFloors.remove(0);
                    int direction;
                    if(elevator.getCurrentFloor() - elevator.getDestinationFloor() < 0){
                        direction = 1;
                    } else{
                        direction = -1;
                    }
                    orderedFloors.forEach(orderedFloor -> {
                        if(checkIfElevatorCanStopOnFloor(orderedFloor, elevator, direction)){
                            if(!elevator.getFloors().contains(orderedFloor)){
                                elevator.getFloors().add(orderedFloor);
                            }
                        }
                    });
                }
            } else {
                int direction;
                if(elevator.getCurrentFloor() - elevator.getDestinationFloor() < 0){
                    direction = 1;
                } else{
                    direction = -1;
                }
                List<Integer> orderedFloors = elevator.getOrderedFloors();
                orderedFloors.forEach(floor -> {
                    if(checkIfElevatorCanStopOnFloor(floor, elevator, direction)){
                        if(!elevator.getFloors().contains(floor)){
                            elevator.getFloors().add(floor);
                        }
                    }
                });
            }
        }
    }

    private void updateElevatorActualFloors(List<Elevator> elevators){
        elevators.forEach(elevator -> {
            Integer actualFloor = elevator.getCurrentFloor();
            Integer destinationFloor = elevator.getDestinationFloor();
            List<Integer> floors = elevator.getFloors();
            if(!floors.contains(actualFloor)){
                if(actualFloor - destinationFloor > 0){
                    elevator.setCurrentFloor(actualFloor-1);
                } else if(actualFloor - destinationFloor < 0){
                    elevator.setCurrentFloor(actualFloor+1);
                }
            } else{
                floors.remove(actualFloor);
                elevator.getOrderedFloors().remove(actualFloor);
            }
        });
    }

    private void updateDestinationFloorsForWaitingFloors(List<Elevator> elevators){
        if(!waitingFloors.isEmpty()) {
            while(isAnyElevatorFree(elevators)) {
                Integer floor = waitingFloors.poll();
                if(floor != null){
                    Elevator elevator = findElevatorForFloor(floor, elevators);
                    elevator.setDestinationFloor(floor);
                    elevator.getFloors().add(floor);
                } else {
                    break;
                }
            }
        }
    }

    private boolean checkIfElevatorCanStopOnFloor(int floor, Elevator elevator, int direction){
        return (elevator.getCurrentFloor() < floor && elevator.getDestinationFloor() > floor && direction == 1)
                || (elevator.getCurrentFloor() > floor && elevator.getDestinationFloor() < floor && direction == -1);
    }

    private Elevator findElevatorForFloor(int floor, List<Elevator> elevators){
        Elevator chosenElevator = null;
        int distance = Integer.MAX_VALUE;
        for(Elevator elevator: elevators) {
            if (elevator.getCurrentFloor() == elevator.getDestinationFloor()) {
                if (Math.abs(floor - elevator.getCurrentFloor()) < distance) {
                    chosenElevator = elevator;
                    distance = Math.abs(floor - elevator.getCurrentFloor());
                }
            }
        }
        return chosenElevator;
    }

    private boolean isAnyElevatorFree(List<Elevator> elevators){
        for(Elevator elevator: elevators){
            if(elevator.getCurrentFloor() == elevator.getDestinationFloor()){
                return true;
            }
        }
        return false;
    }

    private boolean isElevatorForFloorOrdered(int floor, List<Elevator> elevators){
        for(Elevator elevator: elevators){
            if(elevator.getDestinationFloor() == floor
                    || elevator.getFloors().contains(floor)){
                return true;
            }
        }
        return false;
    }

    public Queue<Integer> getWaitingFloors(){
        return waitingFloors;
    }
}
