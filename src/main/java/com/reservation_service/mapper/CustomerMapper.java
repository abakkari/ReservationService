package com.reservation_service.mapper;

import com.reservation_service.bo.Customer;
import com.reservation_service.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto tocustomerDto(Customer customer);
}
