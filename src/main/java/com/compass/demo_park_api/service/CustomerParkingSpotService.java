package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.exception.CpfNotFoundException;
import com.compass.demo_park_api.exception.ReceiptNotFoundException;
import com.compass.demo_park_api.repository.CustomerParkingSpotRepository;
import com.compass.demo_park_api.repository.CustomerRepository;
import com.compass.demo_park_api.repository.projection.ParkingProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerParkingSpotService {

    private final CustomerParkingSpotRepository customerParkingSpotRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerParkingSpot create(CustomerParkingSpot customerParkingSpot) {
        return customerParkingSpotRepository.save(customerParkingSpot);
    }

    @Transactional(readOnly = true)
    public CustomerParkingSpot findByReceipt(String receipt) {
        return customerParkingSpotRepository.findByReceiptAndExitDateIsNull(receipt)
                .orElseThrow( () -> new ReceiptNotFoundException(String.format("Receipt '%s' not found or checkout already completed", receipt)));
    }

    @Transactional(readOnly = true)
    public long getTotalTimesFullParking(String cpf) {
        return customerParkingSpotRepository.countByCustomerCpfAndExitDateIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ParkingProjection> findAllByCustomerCpf(String cpf, Pageable pageable) {

        customerRepository.findByCpf(cpf).orElseThrow( () ->
                new CpfNotFoundException(String.format("Cpf %s not found", cpf)));

        return customerParkingSpotRepository.findByCustomerCpfAndExitDateIsNotNull(cpf, pageable);
    }
}
