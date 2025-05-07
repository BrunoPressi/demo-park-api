package com.compass.demo_park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15_MINUTES = 1.75;

    // 2025-05-06-T14:23:15.616463500
    // 20250506-142315
    public static String generateReceipt() {

        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0,19);

        return receipt
                .replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }



}
