package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CustomerParkingSpotCreateDto {

    @NotBlank(message = "license plate number cannot be empty")
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z],{3}-[0-9]{4}", message = "The vehicle license plate must follow the pattern: 'XXX-0000'")
    private String licensePlateNumber;

    @NotBlank(message = "Car brand cannot be empty")
    private String carBrand;

    @NotBlank(message = "Car model cannot be empty")
    private String carModel;

    @NotBlank(message = "Car color cannot be empty")
    private String carColor;

    @NotBlank(message = "Cpf cannot be empty")
    @Size(min = 11, max = 11, message = "Size must be between 11 and 11")
    @CPF(message = "Invalid cpf")
    private String customerCpf;

}
