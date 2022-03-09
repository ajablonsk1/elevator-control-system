package com.example.elevatorsystem;

import com.example.elevatorsystem.entities.Elevator;
import com.example.elevatorsystem.util.ElevatorEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElevatorEngineTests {
    ElevatorEngine elevatorEngine;

    @BeforeEach
    void setUp(){
        elevatorEngine = new ElevatorEngine();
    }

    @Test
    public void orderElevator(){
        //Given
        Elevator elevator1 = new Elevator(1L, 3, 3, new ArrayList<>(), new ArrayList<>());
        Elevator elevator2 = new Elevator(2L, 0, 0, new ArrayList<>(), new ArrayList<>());
        Elevator elevator3 = new Elevator(3L, 8, 8, new ArrayList<>(), new ArrayList<>());
        List<Elevator> elevators = new ArrayList<>();

        //When
        elevators.add(elevator1);
        elevators.add(elevator2);
        elevators.add(elevator3);
        elevatorEngine.orderElevator(2, 1, elevators);
        elevatorEngine.orderElevator(10, -1, elevators);
        elevatorEngine.orderElevator(1, 1, elevators);
        elevatorEngine.step(elevators);

        //Then
        assertEquals(2, elevator1.getDestinationFloor());
        assertEquals(1, elevator2.getDestinationFloor());
        assertEquals(10, elevator3.getDestinationFloor());
    }

    @Test
    public void updateElevatorActualFloors(){
        //Given
        Elevator elevator1 = new Elevator(1L, 3, 3, new ArrayList<>(), new ArrayList<>());
        Elevator elevator2 = new Elevator(2L, 0, 0, new ArrayList<>(), new ArrayList<>());
        Elevator elevator3 = new Elevator(3L, 8, 8, new ArrayList<>(), new ArrayList<>());
        List<Elevator> elevators = new ArrayList<>();

        //When
        elevators.add(elevator1);
        elevators.add(elevator2);
        elevators.add(elevator3);
        elevatorEngine.orderElevator(5, 1, elevators);
        elevatorEngine.orderElevator(11, 1, elevators);
        elevatorEngine.orderElevator(1, -1, elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);

        //Then
        assertEquals(5, elevator1.getCurrentFloor());
        assertEquals(1, elevator2.getCurrentFloor());
        assertEquals(10, elevator3.getCurrentFloor());
    }

    @Test
    public void updateWaitingFloorsIfAllElevatorsAreTaken(){
        //Given
        Elevator elevator1 = new Elevator(1L, 3, 8, new ArrayList<>(), new ArrayList<>());
        Elevator elevator2 = new Elevator(2L, 0, 2, new ArrayList<>(), new ArrayList<>());
        Elevator elevator3 = new Elevator(3L, 7, 9, new ArrayList<>(), new ArrayList<>());
        List<Elevator> elevators = new ArrayList<>();

        //When
        elevators.add(elevator1);
        elevators.add(elevator2);
        elevators.add(elevator3);
        elevatorEngine.orderElevator(6, 1, elevators);
        elevatorEngine.orderElevator(9, -1, elevators);
        elevatorEngine.orderElevator(1, 1, elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);

        //Then
        assertTrue(elevator1.getFloors().contains(6));
        assertEquals(1, elevator2.getCurrentFloor());
        assertFalse(elevator2.getFloors().contains(1));
        assertFalse(elevator3.getFloors().contains(9));
    }

    @Test
    public void waitOneStepOnOrderedFloor(){
        //Given
        Elevator elevator1 = new Elevator(1L, 3, 8, new ArrayList<>(), new ArrayList<>());
        List<Elevator> elevators = new ArrayList<>();

        //When
        elevators.add(elevator1);
        elevatorEngine.orderElevator(6, 1, elevators);
        elevatorEngine.orderElevator(9, -1, elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);

        //Then
        assertEquals(7, elevator1.getCurrentFloor());
        assertTrue(elevatorEngine.getWaitingFloors().contains(9));
    }

    @Test
    public void orderedFloorsTest(){
        //Given
        Elevator elevator1 = new Elevator(1L, 3, 8, new ArrayList<>(), new ArrayList<>());
        Elevator elevator2 = new Elevator(2L, 2, 2, new ArrayList<>(), new ArrayList<>());
        List<Elevator> elevators = new ArrayList<>();

        //When
        elevators.add(elevator1);
        elevators.add(elevator2);
        elevator1.getOrderedFloors().add(4);
        elevator2.getOrderedFloors().add(8);
        elevator2.getOrderedFloors().add(6);
        elevatorEngine.step(elevators);
        elevatorEngine.step(elevators);

        //Then
        assertEquals(4, elevator1.getCurrentFloor());
        assertEquals(4, elevator2.getCurrentFloor());
        assertEquals(8, elevator2.getDestinationFloor());
        assertTrue(elevator2.getFloors().contains(6));
    }
}
