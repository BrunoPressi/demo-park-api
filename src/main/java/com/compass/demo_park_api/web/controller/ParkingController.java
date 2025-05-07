package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.service.ParkingService;
import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotResponseDto;
import com.compass.demo_park_api.web.dto.mapper.CustomerParkingSpotMapper;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Parking Resource", description = "Operations related to the parking feature")
@RestController
@RequestMapping("api/v1/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @Operation(summary = "Check In resource", description = "Resource to check in to a parking lot. " +
            "Requires authentication with a Bearer Token. Access restricted to the 'ADMIN' profile",
            security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerParkingSpotResponseDto.class)),
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URI resource created")),
                @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "404", description = "Cpf or vacancy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/checkIn")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerParkingSpotResponseDto> checkIn(@RequestBody @Valid CustomerParkingSpotCreateDto dto) {

        CustomerParkingSpot customerParkingSpot = CustomerParkingSpotMapper.toCustomerParkingSpot(dto);
        parkingService.checkIn(customerParkingSpot);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(customerParkingSpot.getReceipt())
                .toUri();

        return ResponseEntity.created(location).body(CustomerParkingSpotMapper.toDto(customerParkingSpot));
    }

}
