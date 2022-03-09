package com.example.elevatorsystem.util.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderInElevator {
    private Long id;
    private int floor;
}
