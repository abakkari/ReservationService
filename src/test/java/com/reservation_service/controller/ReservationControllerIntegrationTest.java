package com.reservation_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation_service.bo.Customer;
import com.reservation_service.dto.ReservationDto;
import com.reservation_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeAll
    void setup() {
        Customer customer = new Customer();
        customer.setId(1L);
        customerRepository.save(customer);
    }

    @Test
    void testCreateReservation() throws Exception {
        ReservationDto reservationDTO = new ReservationDto();
        reservationDTO.setCustomerId(1L);
        reservationDTO.setTableId(1L);
        reservationDTO.setReservationDate(LocalDate.of(4025, 10, 25));
        reservationDTO.setStartTime(LocalTime.of(10, 19, 0));
        reservationDTO.setEndTime(LocalTime.of(12, 21, 0));

        String reservationJson = objectMapper.writeValueAsString(reservationDTO);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }
}
