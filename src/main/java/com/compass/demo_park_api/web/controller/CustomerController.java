package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.jwt.JwtUserDetails;
import com.compass.demo_park_api.service.CustomerService;
import com.compass.demo_park_api.service.UserService;
import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerResponseDto;
import com.compass.demo_park_api.web.dto.UserResponseDto;
import com.compass.demo_park_api.web.dto.mapper.CustomerMapper;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Resource", description = "Customer related operations")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    @Operation(summary = "Create new customer", description = "Resource to create a new customer linked to a registered user. " +
            "Request requires the use of a Bearer Token. Access restricted to Role='CUSTOMER'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Customer already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Feature not allowed for ADMIN profile",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerCreateDto customerDto
            , @AuthenticationPrincipal JwtUserDetails userDetails) {

        Customer customer = CustomerMapper.toCustomer(customerDto);

        customer.setUser(userService.findById(userDetails.getId()));
        customerService.createCustomer(customer);

        return ResponseEntity.ok().body(CustomerMapper.toDto(customer));

    }

}
