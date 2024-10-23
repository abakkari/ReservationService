package com.reservation_service.service;

import com.reservation_service.bo.Customer;
import com.reservation_service.dto.CustomerDto;


public interface CustomerService {
    Customer getCustomerById(Long id) throws Exception;

    CustomerDto createCustomer(CustomerDto customerDto);
}