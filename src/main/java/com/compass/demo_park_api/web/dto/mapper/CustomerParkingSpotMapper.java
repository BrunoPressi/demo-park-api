package com.compass.demo_park_api.web.dto.mapper;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerParkingSpotMapper {

    public static CustomerParkingSpot toCustomerParkingSpot(CustomerParkingSpotCreateDto customerParkingSpotCreateDto) {

        return new ModelMapper().map(customerParkingSpotCreateDto, CustomerParkingSpot.class);

    }

    public static CustomerParkingSpotResponseDto toDto(CustomerParkingSpot customerParkingSpot) {

        return new ModelMapper().map(customerParkingSpot, CustomerParkingSpotResponseDto.class);

    }

}
