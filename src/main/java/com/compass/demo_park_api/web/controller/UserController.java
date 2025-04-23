package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.entity.User;
import com.compass.demo_park_api.service.UserService;
import com.compass.demo_park_api.web.dto.UserCreateDto;
import com.compass.demo_park_api.web.dto.UserResponseDto;
import com.compass.demo_park_api.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto createDto) {
        User user = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponseDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDto> usersDto = users.stream().map((x) -> UserMapper.toDto(x)).toList();
        return ResponseEntity.ok(usersDto);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponseDto> updatePassword(@PathVariable Long id, @RequestBody UserCreateDto createDto) {
        User user = userService.updatePassword(id, createDto.getPassword());
        UserResponseDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

}
