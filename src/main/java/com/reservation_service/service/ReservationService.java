package com.reservation_service.service;

import com.reservation_service.dto.ReservationDto;

import java.util.List;

public interface ReservationService {

    public String createReservation(ReservationDto reservationDto) throws Exception;

    public ReservationDto updateReservation(ReservationDto reservationDto);

    public ReservationDto cancelReservation(Long id);

    public List<ReservationDto> getReservationsByCustomerId(Long customerId);

    ReservationDto getReservationById(Long id);
}
