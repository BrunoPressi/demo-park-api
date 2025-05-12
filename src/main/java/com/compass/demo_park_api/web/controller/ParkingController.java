package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.CustomerParkingSpot;
import com.compass.demo_park_api.jwt.JwtUserDetails;
import com.compass.demo_park_api.repository.projection.ParkingProjection;
import com.compass.demo_park_api.service.CustomerParkingSpotService;
import com.compass.demo_park_api.service.CustomerService;
import com.compass.demo_park_api.service.JasperService;
import com.compass.demo_park_api.service.ParkingService;
import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotResponseDto;
import com.compass.demo_park_api.web.dto.PageableDto;
import com.compass.demo_park_api.web.dto.mapper.CustomerParkingSpotMapper;
import com.compass.demo_park_api.web.dto.mapper.PageableMapper;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(name = "Parking Resource", description = "Operations related to the parking feature")
@RestController
@RequestMapping("api/v1/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final CustomerParkingSpotService customerParkingSpotService;
    private final CustomerService customerService;
    private final JasperService jasperService;

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

    @Operation(summary = "Find a customer parking spot by receipt", description = "Request requires authentication by Bearer Token." +
            "Restricted to a 'ADMIN' profile",
            security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerParkingSpotResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not found or checkout already completed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/checkIn/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<CustomerParkingSpotResponseDto> findByReceipt(@PathVariable String receipt) {

        CustomerParkingSpot customerParkingSpot= customerParkingSpotService.findByReceipt(receipt);

        return ResponseEntity.ok().body(CustomerParkingSpotMapper.toDto(customerParkingSpot));

    }

    @Operation(summary = "Resource to a parking check-out", description = "Requires authentication by a Bearer Token." +
            "Restricted to a 'ADMIN' profile.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerParkingSpotResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Receipt not found or checkout already completed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/checkOut/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CustomerParkingSpotResponseDto> checkOut(@PathVariable String receipt) {

        CustomerParkingSpot customerParkingSpot = parkingService.checkOut(receipt);

        return ResponseEntity.ok().body(CustomerParkingSpotMapper.toDto(customerParkingSpot));

    }

    @Operation(summary = "Find all parking's by customer CPF", description = "Request requires authentication by a Bearer Token." +
            "Access restricted a 'ADMIN' profile.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                @Parameter(in = ParameterIn.QUERY, name = "cpf", description = "Customer CPF number",
                        required = true),
                @Parameter(in = ParameterIn.QUERY, name = "page",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                        description = "Represents the returned page"),
                @Parameter(in = ParameterIn.QUERY, name = "size",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                        description = "Represents the total number of elements per page"),
                @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true,
                        content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                        description = "Represents the ordering of the results")
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerParkingSpotResponseDto.class)))),
                @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "404", description = "Cpf not found",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAllParkingsByCpf(@PathVariable String cpf
            ,@Parameter(hidden = true) @PageableDefault(size = 5, sort = "entryDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ParkingProjection> list = customerParkingSpotService.findAllParkingsByCustomerCpf(cpf, pageable);

        return ResponseEntity.ok().body(PageableMapper.toDto(list));
    }

    @Operation(summary = "Find all parking's by a customer ID", description = "Request required authentication by a Bearer Token." +
            "Access restricted to 'CUSTOMER' profile",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                @Parameter(in = ParameterIn.QUERY, name = "page",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                        description = "Represents the returned page"),
                @Parameter(in = ParameterIn.QUERY, name = "size",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                        description = "Represents the total number of elements per page"),
                @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true,
                        content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                        description = "Represents the ordering of the results")
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerParkingSpotResponseDto.class)))),
                @ApiResponse(responseCode = "403", description = "Access denied to admin profile",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PageableDto> getAllParkingsByCustomerUserId(@Parameter(hidden = true) @PageableDefault(size = 5, sort = "entryDate", direction = Sort.Direction.ASC) @AuthenticationPrincipal JwtUserDetails user, Pageable pageable) {

        Page<ParkingProjection> list = customerParkingSpotService.findAllParkingsByCustomerUserId(user.getId(), pageable);

        return ResponseEntity.ok().body(PageableMapper.toDto(list));
    }

    @Operation(summary = "Get parking report", description = "Resource to get a parking report." +
            "Request requires authentication by a Bearer Token. Restricted to 'CUSTOMER' profile",
            security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/report")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> getReport(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {

        String cpf = customerService.findByUserId(user.getId()).getCpf();
        jasperService.addParams("cpf", cpf);

        byte[] bytes = jasperService.generatePdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }

}
