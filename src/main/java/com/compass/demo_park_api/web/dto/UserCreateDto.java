package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class UserCreateDto {

    @NotBlank(message = "email cannot be blank")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 6, message = "size must be between 6 and 6")
    private String password;

}
