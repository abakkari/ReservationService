package com.reservation_service.service;

import com.reservation_service.bo.Customer;
import com.reservation_service.dto.CustomerDto;
import com.reservation_service.mapper.CustomerMapper;
import com.reservation_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public Customer getCustomerById(Long id) throws Exception {
        // Exception to be changed to a custom exception and not a genric one
        return customerRepository.findById(id).orElseThrow(() -> new Exception("Not customer found with id : " + id));
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customerRepository.save(customer);

        return customerMapper.tocustomerDto(customer);
    }
}

