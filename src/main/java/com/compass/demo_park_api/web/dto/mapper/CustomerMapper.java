package com.compass.demo_park_api.web.dto.mapper;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CustomerMapper {

    public static Customer toCustomer(CustomerCreateDto customer) {
        return new ModelMapper().map(customer, Customer.class);
    }

    public static CustomerResponseDto toDto(Customer customer) {
        return new ModelMapper().map(customer, CustomerResponseDto.class);
    }

    public static List<CustomerResponseDto> toListDto(List<Customer> customerList) {
        return customerList.stream().map( (x) -> CustomerMapper.toDto(x) ).toList();
    }

}
