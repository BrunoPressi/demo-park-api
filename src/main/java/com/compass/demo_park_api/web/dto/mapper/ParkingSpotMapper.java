package com.compass.demo_park_api.web.dto.mapper;

import com.compass.demo_park_api.entity.ParkingSpot;
import com.compass.demo_park_api.web.dto.ParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.ParkingSpotResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingSpotMapper {

    public static ParkingSpot toParkingSpot(ParkingSpotCreateDto parkingSpotCreateDto) {
        return new ModelMapper().map(parkingSpotCreateDto, ParkingSpot.class);
    }

    public static ParkingSpotResponseDto toDto(ParkingSpot parkingSpot) {
        return new ModelMapper().map(parkingSpot, ParkingSpotResponseDto.class);
    }

}
