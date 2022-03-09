package com.example.elevatorsystem;

import com.example.elevatorsystem.entities.Elevator;
import com.example.elevatorsystem.repositories.ElevatorRepo;
import com.example.elevatorsystem.services.ElevatorService;
import com.example.elevatorsystem.util.ElevatorEngine;
import com.example.elevatorsystem.util.classes.Order;
import com.example.elevatorsystem.util.classes.OrderInElevator;
import com.example.elevatorsystem.util.classes.Status;
import com.example.elevatorsystem.util.exceptions.ElevatorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ElevatorServiceTest {
    private ElevatorService elevatorService;
    @Mock private ElevatorRepo elevatorRepo;
    @Mock private ElevatorEngine elevatorEngine;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        elevatorService = new ElevatorService(elevatorRepo, elevatorEngine);
    }

    @Test
    void saveElevator() {
        //Given
        Elevator elevator = new Elevator(null, 1, 1,
                new ArrayList<>(), new ArrayList<>());

        //When
        elevatorService.saveElevator(elevator);

        //Then
        ArgumentCaptor<Elevator> elevatorArgumentCaptor = ArgumentCaptor.forClass(Elevator.class);

        verify(elevatorRepo).save(elevatorArgumentCaptor.capture());

        Elevator capturedElevator = elevatorArgumentCaptor.getValue();

        assertThat(capturedElevator).isEqualTo(elevator);
    }

    @Test
    void getElevators() {
        //When
        elevatorService.getElevators();

        //Then
        verify(elevatorRepo).findAll();
    }

    @Test
    void updateElevator() throws ElevatorNotFoundException {
        //Given
        Status status = new Status(1L,1,1);

        given(elevatorRepo.findElevatorById(status.getId()))
                .willReturn(Optional.of(new Elevator()));

        //When
        elevatorService.updateElevator(status);

        //Then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(elevatorRepo).findElevatorById(idArgumentCaptor.capture());

        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(status.getId());
    }

    @Test
    void updateElevatorThrow(){
        //Given
        Status status = new Status(1L,1,1);
        //When
        //Then
        assertThatThrownBy(() -> elevatorService.updateElevator(status))
                .isInstanceOf(ElevatorNotFoundException.class)
                .hasMessageContaining("Elevator with given id doesn't exist!");
    }

    @Test
    void step() {
        //Given
        List<Elevator> elevators = new ArrayList<>();
        given(elevatorRepo.findAll())
                .willReturn(elevators);

        //When
        elevatorService.step();

        //Then
        ArgumentCaptor<List<Elevator>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(elevatorRepo).findAll();
        verify(elevatorEngine).step(listArgumentCaptor.capture());
        verify(elevatorRepo).saveAll(listArgumentCaptor.capture());

        List<List<Elevator>> results = listArgumentCaptor.getAllValues();

        assertThat(results.get(0)).isEqualTo(elevators);
        assertThat(results.get(1)).isEqualTo(elevators);

    }

    @Test
    void orderElevator() {
        //Given
        List<Elevator> elevators = new ArrayList<>();
        Order order = new Order(1, "Up");
        given(elevatorRepo.findAll())
                .willReturn(elevators);
        //When
        elevatorService.orderElevator(order);

        //Then
        verify(elevatorRepo).findAll();

        ArgumentCaptor<List<Elevator>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> directionArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> orderArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(elevatorEngine).orderElevator(orderArgumentCaptor.capture(),
                directionArgumentCaptor.capture(),
                listArgumentCaptor.capture());

        List<Elevator> capturedList = listArgumentCaptor.getValue();
        Integer capturedDirection = directionArgumentCaptor.getValue();
        Integer capturedOrder = orderArgumentCaptor.getValue();

        assertThat(capturedList).isEqualTo(elevators);
        assertThat(capturedDirection).isEqualTo(1);
        assertThat(capturedOrder).isEqualTo(order.getFloor());

    }

    @Test
    void orderFloorInElevator() throws ElevatorNotFoundException {
        //Given
        OrderInElevator order = new OrderInElevator(1L,1);

        Elevator elevator = new Elevator();

        given(elevatorRepo.findElevatorById(order.getId()))
                .willReturn(Optional.of(elevator));

        //When
        elevatorService.orderFloorInElevator(order);

        //Then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Elevator> elevatorArgumentCaptor = ArgumentCaptor.forClass(Elevator.class);

        verify(elevatorRepo).findElevatorById(idArgumentCaptor.capture());
        verify(elevatorRepo).save(elevatorArgumentCaptor.capture());

        Long capturedId = idArgumentCaptor.getValue();
        Elevator capturedElevator = elevatorArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(order.getId());
        assertThat(capturedElevator).isEqualTo(elevator);
    }

    @Test
    void orderFloorInElevatorThrow() {
        //Given
        OrderInElevator order = new OrderInElevator(1L,1);
        //When
        //Then
        assertThatThrownBy(() -> elevatorService.orderFloorInElevator(order))
                .isInstanceOf(ElevatorNotFoundException.class)
                .hasMessageContaining("Elevator with given id doesn't exist!");
    }

    @Test
    void deleteElevator() throws ElevatorNotFoundException {
        //Given
        Long id = 1L;
        Elevator elevator = new Elevator();
        given(elevatorRepo.findElevatorById(id))
                .willReturn(Optional.of(elevator));

        //When
        elevatorService.deleteElevator(id);

        //Then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Elevator> elevatorArgumentCaptor = ArgumentCaptor.forClass(Elevator.class);

        verify(elevatorRepo).findElevatorById(idArgumentCaptor.capture());
        verify(elevatorRepo).delete(elevatorArgumentCaptor.capture());

        Long capturedId = idArgumentCaptor.getValue();
        Elevator capturedElevator = elevatorArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(capturedElevator).isEqualTo(elevator);
    }

    @Test
    void deleteElevatorThrow() {
        //Given
        Long id = 1L;
        //When
        //Then
        assertThatThrownBy(() -> elevatorService.deleteElevator(id))
                .isInstanceOf(ElevatorNotFoundException.class)
                .hasMessageContaining("Elevator with given id doesn't exist!");
    }
}