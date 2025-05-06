package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.repository.CustomerParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerParkingSpotService {

    private final CustomerParkingSpotRepository customerParkingSpotRepository;

    @Transactional
    public void create(CustomerParkingSpot customerParkingSpot) {
        customerParkingSpotRepository.save(customerParkingSpot);
    }

}
