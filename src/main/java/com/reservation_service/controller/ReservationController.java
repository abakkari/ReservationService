package com.reservation_service.controller;

import com.reservation_service.dto.ReservationDto;
import com.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody ReservationDto reservationDto) throws Exception {
        // Exception to be changed to a custom exception and not a genric one
        String response = reservationService.createReservation(reservationDto);
        HttpStatus status = switch (response) {
            case "The restaurant is fully booked for the selected time slot" -> HttpStatus.OK;
            case "Error while creating reservation" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.CREATED;
        };
        return new ResponseEntity<>(response, status);
    }

    @PutMapping
    public ResponseEntity<ReservationDto> updateReservation(@RequestBody ReservationDto reservationDto) {
        ReservationDto updatedReservation = reservationService.updateReservation(reservationDto);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable Long id) {
        ReservationDto canceledReservation = reservationService.cancelReservation(id);
        if(canceledReservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(canceledReservation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        ReservationDto reservation = reservationService.getReservationById(id);
        if(reservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCustomerId(@PathVariable Long customerId) {
        List<ReservationDto> reservations = reservationService.getReservationsByCustomerId(customerId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

}
