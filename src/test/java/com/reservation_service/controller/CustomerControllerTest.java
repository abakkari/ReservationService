package com.reservation_service.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reservation_service.dto.CustomerDto;
import com.reservation_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        customerController = new CustomerController(customerService);
    }

    @Test
    void createCustomer_ReturnsCreatedCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setPhoneNumber("1234567890");

        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.createCustomer(customerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void createCustomer_ReturnsBadRequestWhenCustomerIsNull() {
        ResponseEntity<CustomerDto> response = customerController.createCustomer(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}