package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.exception.CpfUniqueViolationException;
import com.compass.demo_park_api.repository.CustomerRepository;
import com.compass.demo_park_api.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void createCustomer(Customer customer) {
        try {
            customerRepository.save(customer);
        }
        catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("Customer %s already exists", customer.getName()));
        }

    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> {
             throw new CustomerNotFoundException("Customer not found");
        });
    }

}
