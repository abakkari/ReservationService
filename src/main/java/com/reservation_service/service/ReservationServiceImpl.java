package com.reservation_service.service;

import com.reservation_service.bo.Reservation;
import com.reservation_service.dto.ReservationDto;
import com.reservation_service.dto.SendReservationDto;
import com.reservation_service.mapper.ReservationMapper;
import com.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CustomerService customerService;
    private final RestTemplate restTemplate;
    @Value("${restaurant.url}")
    private String restaurantUrl;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String createReservation(ReservationDto reservationDto) throws Exception {
        // Exception to be changed to a custom exception and not a genric one
        String url = restaurantUrl + "/availability/is-available?tableId=" + reservationDto.getTableId() + "&availabilityDate=" +
                reservationDto.getReservationDate() + "&startTime=" + reservationDto.getStartTime() + "&endTime=" + reservationDto.getEndTime();
        Boolean response = restTemplate.getForObject(url, Boolean.class);
        String result;
        if(response != null){
            if(response){
                Reservation reservation = new Reservation();
                reservation.setCustomer(customerService.getCustomerById(reservationDto.getCustomerId()));
                reservation.setReservationDate(reservationDto.getReservationDate());
                reservation.setStartTime(reservationDto.getStartTime());
                reservation.setEndTime(reservationDto.getEndTime());
                //reservation.setRestaurantId(reservationDto.getRestaurantId());
                reservation.setTableId(reservationDto.getTableId());
                reservation.setStatus("Confirmed");
                reservationRepository.save(reservation);

                sendReservationToRestaurant(reservation);
                result = "reservation created";
            } else{
                result = "The restaurant is fully booked for the selected time slot";
            }
        } else {
            result = "Error while creating reservation";
        }
        return result;
    }

    private void sendReservationToRestaurant(Reservation reservation) {
        SendReservationDto sendReservationDto = new SendReservationDto();
        sendReservationDto.setAvailabilityDate(reservation.getReservationDate());
        sendReservationDto.setStartTime(reservation.getStartTime());
        sendReservationDto.setEndTime(reservation.getEndTime());
        sendReservationDto.setTableId(reservation.getTableId());
        sendReservationDto.setReservationId(reservation.getId());

        String url = restaurantUrl + "/availability";
        restTemplate.postForEntity(url, sendReservationDto, String.class);

    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        //todo implement update reservation
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ReservationDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if(reservation != null){
            reservation.setStatus("Canceled");
            reservationRepository.save(reservation);

            sendCancelReservationToRestaurant(reservation.getId());

            return reservationMapper.toReservationDto(reservation);
        }
        return null;
    }

    private void sendCancelReservationToRestaurant(Long id) {
        String url = restaurantUrl + "/availability/" + id;
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Exception occurred while sending reservation cancellation to restaurant", e);
        }
    }

    @Override
    public List<ReservationDto> getReservationsByCustomerId(Long clientId) {
        return reservationMapper.toReservationDtos(reservationRepository.findByCustomerId(clientId));
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        return reservationMapper.toReservationDto(reservationRepository.findById(id).orElse(null));
    }

}
