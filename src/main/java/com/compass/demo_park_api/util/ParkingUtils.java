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

    private static final double PERCENTAGE_DISCOUNT = 0.30;

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

    public static BigDecimal calculateTotalValue(LocalDateTime entry, LocalDateTime exit) {
        long minutes = entry.until(exit, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {

            total = FIRST_15_MINUTES;

        } else if (minutes <= 60) {

            total = FIRST_60_MINUTES;

        } else {

            double tempoExtra = (double) (minutes - 60) / 15;
            total = FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * Math.ceil(tempoExtra));

        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal cost, long numberOfTimes) {


        BigDecimal discount = null;

        if (numberOfTimes % 10 == 0 && numberOfTimes >= 10) {
            discount = cost.multiply(BigDecimal.valueOf(PERCENTAGE_DISCOUNT));
        }
        else {
            discount = BigDecimal.valueOf(0.00);
        }

        return discount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
