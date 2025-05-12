package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ParkingSpotCreateDto {

    @NotBlank(message = "{NotBlank.parkingSpotCreateDto.code}")
    @Size(min = 4, max = 4, message = "{Size.parkingSpotCreateDto.code}")
    private String code;



    @NotBlank(message = "{NotBlank.parkingSpotCreateDto.status}")
    @Pattern(regexp = "FREE|OCCUPIED", message = "{Pattern.parkingSpotCreateDto.status}")
    private String status;
}
