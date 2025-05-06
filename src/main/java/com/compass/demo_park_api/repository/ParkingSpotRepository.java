package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.ParkingSpot;
import com.compass.demo_park_api.entity.enums.ParkingSpotStatus;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    Optional<ParkingSpot> findByCode(String code);

    Optional<ParkingSpot> findFirstByStatus(ParkingSpotStatus parkingSpotStatus);
}
