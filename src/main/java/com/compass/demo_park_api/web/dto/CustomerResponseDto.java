package com.compass.demo_park_api.web.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String cpf;

}
