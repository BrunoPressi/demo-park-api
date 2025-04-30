package com.compass.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CustomerCreateDto {

    @NotBlank(message = "Cpf cannot be empty")
    @Size(min = 11, max = 11, message = "Size must be between 11 and 11")
    @CPF(message = "Invalid cpf")
    private String cpf;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 100, message = "Size must be between 5 and 100")
    private String name;

}
