package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.ParkingSpot;
import com.compass.demo_park_api.exception.CodeNotFoundException;
import com.compass.demo_park_api.exception.CodeUniqueViolationException;
import com.compass.demo_park_api.exception.UserNotFoundException;
import com.compass.demo_park_api.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.compass.demo_park_api.entity.enums.ParkingSpotStatus.FREE;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public void save(ParkingSpot parkingSpot) {
        try {
            parkingSpotRepository.save(parkingSpot);
        }
        catch (DataIntegrityViolationException e) {
            throw new CodeUniqueViolationException(String.format("Parking spot %s already exists!", parkingSpot.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpot findByCode(String code) {

        return parkingSpotRepository.findByCode(code).orElseThrow(
                () -> new CodeNotFoundException(String.format("Code '%s' not found", code))
        );

    }

    @Transactional(readOnly = true)
    public ParkingSpot findFreeParkingSpot() {
        return parkingSpotRepository.findFirstByStatus(FREE)
                .orElseThrow( () -> new CodeNotFoundException("No vacancies found"));
    }
}
