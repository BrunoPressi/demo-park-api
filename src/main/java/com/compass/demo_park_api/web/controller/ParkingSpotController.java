package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.ParkingSpot;
import com.compass.demo_park_api.repository.ParkingSpotRepository;
import com.compass.demo_park_api.service.ParkingSpotService;
import com.compass.demo_park_api.web.dto.ParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.ParkingSpotResponseDto;
import com.compass.demo_park_api.web.dto.mapper.ParkingSpotMapper;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Parking Spot", description = "Parking Spot related operations")
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/parking-spot")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @Operation(summary = "Create a new parking spot", description = "Resource to create a new parking spot." +
            "Request required a Bearer Token. Restricted to ADMIN profile",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSpotResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Invalid input data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ParkingSpotCreateDto parkingSpotCreateDto) {

        ParkingSpot parkingSpot = ParkingSpotMapper.toParkingSpot(parkingSpotCreateDto);
        parkingSpotService.save(parkingSpot);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(parkingSpot.getCode())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find parking spot by code", description = "Resource to find a parking spot by code." +
            "Request required a Bearer Token. Restricted to ADMIN profile",
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSpotResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Code not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSpotResponseDto.class)))
            }
    )
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpotResponseDto> findByCode(@PathVariable String code) {
        ParkingSpot parkingSpot = parkingSpotService.findByCode(code);

        return ResponseEntity.ok().body(ParkingSpotMapper.toDto(parkingSpot));
    }

}
