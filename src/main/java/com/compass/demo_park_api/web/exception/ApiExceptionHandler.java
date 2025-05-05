package com.compass.demo_park_api.web.exception;

import com.compass.demo_park_api.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                        HttpServletRequest request) {
        BindingResult result = e.getBindingResult();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Field(s)", result));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException e,
                                                                        HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(HttpServletRequest request,
                                                                        UsernameUniqueViolationException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT,  e.getMessage()));
    }

    @ExceptionHandler(CpfUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> cpfUniqueViolationException(HttpServletRequest request,
                                                                    CpfUniqueViolationException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT,  e.getMessage()));
    }

    @ExceptionHandler(CodeUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> codeUniqueViolationException(HttpServletRequest request,
                                                                     CodeUniqueViolationException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, e.getMessage()));

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(HttpServletRequest request,
                                                              UserNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND,  e.getMessage()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorMessage> customerNotFoundException(HttpServletRequest request,
                                                                  CustomerNotFoundException e) {

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<ErrorMessage> codeNotFoundException(HttpServletRequest request,
                                                              CodeNotFoundException e) {

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(HttpServletRequest request,
                                                                 PasswordInvalidException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
