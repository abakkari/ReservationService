package com.reservation_service.mapper;

import com.reservation_service.bo.Reservation;
import com.reservation_service.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "customerId", source = "customer.id")
    ReservationDto toReservationDto(Reservation reservation);

    List<ReservationDto> toReservationDtos(List<Reservation> reservations);
}
