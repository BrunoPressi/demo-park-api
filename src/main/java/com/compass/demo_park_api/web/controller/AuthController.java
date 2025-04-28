package com.compass.demo_park_api.web.controller;

import com.compass.demo_park_api.jwt.JwtToken;
import com.compass.demo_park_api.jwt.JwtUserDetailsService;
import com.compass.demo_park_api.web.dto.UserLoginDto;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AuthController {

    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authManager;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody @Valid UserLoginDto user, HttpServletRequest request) {

        log.info("Authenticate process {}", user.getUsername());

        try {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            authManager.authenticate(authenticationToken);
            JwtToken token = userDetailsService.getTokenAuthenticated(user.getUsername());

            return ResponseEntity.ok(token);
        }
        catch (AuthenticationException e) {
            log.warn("Bad Credentials from username {}", user.getUsername());
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

}
