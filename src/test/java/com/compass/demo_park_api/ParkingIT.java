package com.compass.demo_park_api;

import com.compass.demo_park_api.web.dto.CustomerParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotResponseDto;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCustomerParkingSpot_withAdminProfileAndValidData_returnCustomerParkingSpotResponseDtoWithStatus201() {

        CustomerParkingSpotResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/parking/checkIn")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new CustomerParkingSpotCreateDto
                    ("ABC-0000", "Chevrolet", "Vectra 1.0", "White", "50339980052"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerParkingSpotResponseDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getCustomerCpf()).isEqualTo("50339980052");

    }

    @Test
    public void createCustomerParkingSpot_withCustomerProfileAndValidData_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking/checkIn")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jim@gmail.com", "123456"))
                .bodyValue(new CustomerParkingSpotCreateDto
                        ("ABC-0000", "Chevrolet", "Vectra 1.0", "White", "50339980052"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void createCustomerParkingSpot_withInvalidData_returnErrorMessageWithStatus422() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking/checkIn")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new CustomerParkingSpotCreateDto
                        ("ABC-000", "", "", "", "0479302600111"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
            .bodyValue(new CustomerParkingSpotCreateDto
                    ("ABC-00000", "", "", "", "047930260"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createCustomerParkingSpot_withNonExistsCpf_returnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking/checkIn")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new CustomerParkingSpotCreateDto
                        ("ABC-0000", "Chevrolet", "Vectra 1.0", "Black", "04793026001"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

}
