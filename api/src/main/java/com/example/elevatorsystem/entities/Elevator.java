package com.example.elevatorsystem.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Elevator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int currentFloor;

    @Column
    private int destinationFloor;

    @ElementCollection
    List<Integer> floors = new ArrayList<>();

    @ElementCollection
    List<Integer> orderedFloors = new ArrayList<>();

    public void updateElevator(int actualFloor,
                               int destinationFloor,
                               List<Integer> floors,
                               List<Integer> orderedFloors){
        setCurrentFloor(actualFloor);
        setDestinationFloor(destinationFloor);
        setFloors(floors);
        setOrderedFloors(orderedFloors);
    }

    public List<Integer> getFloors() {
        return floors;
    }

    public void setFloors(List<Integer> floors) {
        this.floors = floors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int actualFloor) {
        this.currentFloor = actualFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public List<Integer> getOrderedFloors() {
        return orderedFloors;
    }

    public void setOrderedFloors(List<Integer> orderedFloors) {
        this.orderedFloors = orderedFloors;
    }
}
