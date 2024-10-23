package com.reservation_service.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reservation_service.dto.ReservationDto;
import com.reservation_service.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        reservationService = mock(ReservationService.class);
        reservationController = new ReservationController(reservationService);
    }

    @Test
    void createReservation_ReturnsCreatedStatus() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        when(reservationService.createReservation(reservationDto)).thenReturn("Reservation created successfully");

        ResponseEntity<String> response = reservationController.createReservation(reservationDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Reservation created successfully", response.getBody());
    }

    @Test
    void createReservation_ReturnsFullyBookedStatus() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        when(reservationService.createReservation(reservationDto)).thenReturn("The restaurant is fully booked for the selected time slot");

        ResponseEntity<String> response = reservationController.createReservation(reservationDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The restaurant is fully booked for the selected time slot", response.getBody());
    }

    @Test
    void createReservation_ReturnsBadRequestStatus() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        when(reservationService.createReservation(reservationDto)).thenReturn("Error while creating reservation");

        ResponseEntity<String> response = reservationController.createReservation(reservationDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error while creating reservation", response.getBody());
    }

    @Test
    void updateReservation_ReturnsUpdatedReservation() {
        ReservationDto reservationDto = new ReservationDto();
        ReservationDto updatedReservationDto = new ReservationDto();
        when(reservationService.updateReservation(reservationDto)).thenReturn(updatedReservationDto);

        ResponseEntity<ReservationDto> response = reservationController.updateReservation(reservationDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedReservationDto, response.getBody());
    }

    @Test
    void cancelReservation_ReturnsCanceledReservation() {
        Long id = 1L;
        ReservationDto canceledReservationDto = new ReservationDto();
        when(reservationService.cancelReservation(id)).thenReturn(canceledReservationDto);

        ResponseEntity<ReservationDto> response = reservationController.cancelReservation(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(canceledReservationDto, response.getBody());
    }

    @Test
    void getReservationsByCustomerId_ReturnsReservationsList() {
        Long customerId = 1L;
        List<ReservationDto> reservations = List.of(new ReservationDto());
        when(reservationService.getReservationsByCustomerId(customerId)).thenReturn(reservations);

        ResponseEntity<List<ReservationDto>> response = reservationController.getReservationsByCustomerId(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
    }
}