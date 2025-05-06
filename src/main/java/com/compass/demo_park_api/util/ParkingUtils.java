package com.compass.demo_park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

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
