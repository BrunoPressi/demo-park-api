package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerParkingSpotRepository extends JpaRepository<CustomerParkingSpot, Long> {

}
