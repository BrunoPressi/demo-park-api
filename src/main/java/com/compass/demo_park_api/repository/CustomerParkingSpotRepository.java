package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerParkingSpotRepository extends JpaRepository<CustomerParkingSpot, Long> {

    Optional<CustomerParkingSpot> findByReceiptAndExitDateIsNull(String receipt);
}
