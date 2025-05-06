package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CustomerParkingSpotResponseDto {

    private String licensePlateNumber;
    private String carBrand;
    private String carModel;
    private String carColor;
    private String customerCpf;
    private String parkingSpotCode;
    private String receipt;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private BigDecimal value;
    private BigDecimal discount;

}
