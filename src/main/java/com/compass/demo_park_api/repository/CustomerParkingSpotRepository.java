package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.repository.projection.ParkingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerParkingSpotRepository extends JpaRepository<CustomerParkingSpot, Long> {

    Optional<CustomerParkingSpot> findByReceiptAndExitDateIsNull(String receipt);

    long countByCustomerCpfAndExitDateIsNotNull(String cpf);

    Page<ParkingProjection> findByCustomerCpfAndExitDateIsNotNull(String cpf, Pageable pageable);

    Page<ParkingProjection> findByCustomerUserIdAndExitDateIsNotNull(Long id, Pageable pageable);
}
