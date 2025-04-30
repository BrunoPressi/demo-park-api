package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.service.UserService;
import com.compass.demo_park_api.web.dto.UserCreateDto;
import com.compass.demo_park_api.web.dto.UserPasswordDto;
import com.compass.demo_park_api.web.dto.UserResponseDto;
import com.compass.demo_park_api.web.dto.mapper.UserMapper;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Resource", description = "Operations for registering, editing and reading users")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create new user", description = "Resource to create new user",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto createDto) {
        User user = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @Operation(summary = "Find user by id", description = "Request requires a Bearer Token, access restricted to ADMIN|CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "204", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CUSTOMER') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponseDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Find all users", description = "Request requires a Bearer Token, access restricted to ADMIN",
        security = @SecurityRequirement(name = "security"),
        responses = {
                @ApiResponse(responseCode = "200", description = "List with all users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
                @ApiResponse(responseCode = "403", description = "Access Denied",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDto> usersDto = UserMapper.toListDto(users);
        return ResponseEntity.ok(usersDto);
    }

    @Operation(summary = "Update user password", description = "Request requires a Bearer Token, access restricted to ADMIN|CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect password(s)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UserPasswordDto passwordDto) {
        userService.updatePassword(id, passwordDto.getCurrentPassword(), passwordDto.getNewPassword(), passwordDto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

}