package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPasswordDto {

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 6, message = "size must be between 6 and 6")
    private String currentPassword;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 6, message = "size must be between 6 and 6")
    private String newPassword;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 6, message = "size must be between 6 and 6")
    private String confirmPassword;

}
