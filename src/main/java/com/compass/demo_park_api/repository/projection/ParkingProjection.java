package com.compass.demo_park_api.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ParkingProjection {

    String getReceipt();
    String getLicensePlateNumber();
    String getCarBrand();
    String getCarModel();
    String getCarColor();
    String getParkingSpotCode();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
    LocalDateTime getEntryDate();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
    LocalDateTime getExitDate();
    BigDecimal getValue();
    BigDecimal getDiscount();
    String getCustomerCpf();
}
