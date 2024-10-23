package com.reservation_service.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reservation_service.bo.Customer;
import com.reservation_service.dto.CustomerDto;
import com.reservation_service.mapper.CustomerMapper;
import com.reservation_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerMapper = mock(CustomerMapper.class);
        customerServiceImpl = new CustomerServiceImpl(customerRepository, customerMapper);
    }

    @Test
    void getCustomerById_ReturnsCustomer() throws Exception {
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);

        when(customerRepository.findById(id)).thenReturn(java.util.Optional.of(customer));

        Customer result = customerServiceImpl.getCustomerById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getCustomerById_ThrowsExceptionWhenCustomerNotFound() {
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> customerServiceImpl.getCustomerById(id));

        assertEquals("Not customer found with id : " + id, exception.getMessage());
    }

    @Test
    void createCustomer_ReturnsCreatedCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setPhoneNumber("1234567890");

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.tocustomerDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerServiceImpl.createCustomer(customerDto);

        assertNotNull(result);
        assertEquals(customerDto.getName(), result.getName());
    }
}