package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.exception.ReceiptNotFoundException;
import com.compass.demo_park_api.repository.CustomerParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerParkingSpotService {

    private final CustomerParkingSpotRepository customerParkingSpotRepository;

    @Transactional
    public CustomerParkingSpot create(CustomerParkingSpot customerParkingSpot) {
        return customerParkingSpotRepository.save(customerParkingSpot);
    }

    @Transactional(readOnly = true)
    public CustomerParkingSpot findByReceipt(String receipt) {
        return customerParkingSpotRepository.findByReceiptAndExitDateIsNull(receipt)
                .orElseThrow( () -> new ReceiptNotFoundException(String.format("Receipt '%s' not found or checkout already completed", receipt)));
    }

}
