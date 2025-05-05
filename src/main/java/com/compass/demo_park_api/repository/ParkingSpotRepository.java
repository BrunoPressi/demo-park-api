package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    Optional<ParkingSpot> findByCode(String code);
}
