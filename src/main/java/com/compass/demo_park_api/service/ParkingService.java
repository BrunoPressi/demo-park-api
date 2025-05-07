package com.compass.demo_park_api.service;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.entity.ParkingSpot;
import com.compass.demo_park_api.entity.enums.ParkingSpotStatus;
import com.compass.demo_park_api.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ParkingService {

    private final CustomerParkingSpotService customerParkingSpotService;
    private final CustomerService customerService;
    private final ParkingSpotService parkingSpotService;

    @Transactional
    public CustomerParkingSpot checkIn(CustomerParkingSpot customerParkingSpot) {

        Customer customer = customerService.findByCpf(customerParkingSpot.getCustomer().getCpf());
        ParkingSpot parkingSpot = parkingSpotService.findFreeParkingSpot();
        String receipt = ParkingUtils.generateReceipt();

        parkingSpot.setStatus(ParkingSpotStatus.OCCUPIED);

        customerParkingSpot.setEntryDate(LocalDateTime.now());
        customerParkingSpot.setReceipt(receipt);
        customerParkingSpot.setCustomer(customer);
        customerParkingSpot.setParkingSpot(parkingSpot);

        return customerParkingSpotService.create(customerParkingSpot);

    }

    @Transactional
    public CustomerParkingSpot checkOut(String receipt) {

        CustomerParkingSpot customerParkingSpot = customerParkingSpotService.findByReceipt(receipt);

        LocalDateTime exitDate = LocalDateTime.now();

        BigDecimal totalValue = ParkingUtils.calculateTotalValue(customerParkingSpot.getEntryDate(), exitDate);

        customerParkingSpot.setValue(totalValue);

        long totalTimes = customerParkingSpotService.getTotalTimesFullParking(customerParkingSpot.getCustomer().getCpf());

        BigDecimal discount = ParkingUtils.calculateDiscount(totalValue, totalTimes);

        customerParkingSpot.setDiscount(discount);
        customerParkingSpot.setExitDate(exitDate);

        customerParkingSpot.getParkingSpot().setStatus(ParkingSpotStatus.FREE);

        return customerParkingSpotService.create(customerParkingSpot);
    }

}
