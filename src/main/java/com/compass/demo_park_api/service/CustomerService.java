package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.exception.CpfNotFoundException;
import com.compass.demo_park_api.exception.CpfUniqueViolationException;
import com.compass.demo_park_api.repository.CustomerRepository;
import com.compass.demo_park_api.exception.CustomerNotFoundException;
import com.compass.demo_park_api.repository.projection.CustomerProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new CpfUniqueViolationException("Customer already registered");
        }

    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException(String.format("Customer id=%s not found", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<CustomerProjection> findAllCustomers(Pageable pageable) {
        return customerRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Customer findByUserId(Long id) {
        return customerRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Customer findByCpf(String cpf) {
        return customerRepository.findByCpf(cpf).
                orElseThrow( () -> new CpfNotFoundException(String.format("CPF %s not found", cpf)));
    }
}
