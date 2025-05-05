package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ParkingSpotCreateDto {

    @NotBlank
    @Size(min = 4, max = 4, message = "size must be between 4 and 4")
    private String code;

    @NotBlank
    @Pattern(regexp = "FREE|OCCUPIED", message = "must match \\FREE|OCCUPIED\\")
    private String status;
}
