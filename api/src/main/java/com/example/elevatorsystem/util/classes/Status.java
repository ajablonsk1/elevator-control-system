package com.example.elevatorsystem.util.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Status {
    private Long id;
    private int currentFloor;
    private int destinationFloor;
}
