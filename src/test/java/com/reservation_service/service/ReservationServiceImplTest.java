package com.reservation_service.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reservation_service.bo.Customer;
import com.reservation_service.bo.Reservation;
import com.reservation_service.dto.ReservationDto;
import com.reservation_service.mapper.ReservationMapper;
import com.reservation_service.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationMapper = mock(ReservationMapper.class);
        customerService = mock(CustomerService.class);
        restTemplate = mock(RestTemplate.class);
        reservationServiceImpl = new ReservationServiceImpl(reservationRepository, reservationMapper, customerService, restTemplate);
    }

    @Test
    void createReservation_ReturnsReservationCreated() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setReservationDate(LocalDate.of(2025, 10, 25));
        reservationDto.setStartTime(LocalTime.of(10, 10, 0));
        reservationDto.setEndTime(LocalTime.of(10, 10, 0));
        reservationDto.setCustomerId(1L);

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(customerService.getCustomerById(anyLong())).thenReturn(new Customer());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        String result = reservationServiceImpl.createReservation(reservationDto);

        assertEquals("reservation created", result);
    }

    @Test
    void createReservation_ReturnsFullyBooked() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setReservationDate(LocalDate.of(2025, 10, 25));
        reservationDto.setStartTime(LocalTime.of(10, 10, 0));
        reservationDto.setEndTime(LocalTime.of(10, 10, 0));

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        String result = reservationServiceImpl.createReservation(reservationDto);

        assertEquals("The restaurant is fully booked for the selected time slot", result);
    }

    @Test
    void createReservation_ReturnsErrorWhileCreatingReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setReservationDate(LocalDate.of(2025, 10, 25));
        reservationDto.setStartTime(LocalTime.of(10, 10, 0));
        reservationDto.setEndTime(LocalTime.of(10, 10, 0));

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(null);

        String result = reservationServiceImpl.createReservation(reservationDto);

        assertEquals("Error while creating reservation", result);
    }

    @Test
    void cancelReservation_ReturnsCanceledReservation() {
        Long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus("Confirmed");

        when(reservationRepository.findById(id)).thenReturn(java.util.Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toReservationDto(any(Reservation.class))).thenReturn(new ReservationDto());

        ReservationDto result = reservationServiceImpl.cancelReservation(id);

        assertNotNull(result);
        assertEquals("Canceled", reservation.getStatus());
    }

    @Test
    void cancelReservation_ReturnsNullWhenReservationNotFound() {
        Long id = 1L;

        when(reservationRepository.findById(id)).thenReturn(java.util.Optional.empty());

        ReservationDto result = reservationServiceImpl.cancelReservation(id);

        assertNull(result);
    }

    @Test
    void getReservationsByCustomerId_ReturnsReservationsList() {
        Long customerId = 1L;
        List<Reservation> reservations = List.of(new Reservation());

        when(reservationRepository.findByCustomerId(customerId)).thenReturn(reservations);
        when(reservationMapper.toReservationDtos(reservations)).thenReturn(List.of(new ReservationDto()));

        List<ReservationDto> result = reservationServiceImpl.getReservationsByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}