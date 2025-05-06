package com.compass.demo_park_api;

import com.compass.demo_park_api.web.dto.ParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.ParkingSpotResponseDto;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking-spot/parking-spot-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking-spot/parking-spot-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingSpotIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createParkingSpot_withAdminProfileAndValidData_returnLocationUriWithStatus201() {

        testClient
                .post()
                .uri("api/v1/parking-spot")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("A-05", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);

    }

    @Test
    public void createParkingSpot_invalidData_returnErrorMessageWithStatus422() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking-spot")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("A1-", "test"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/parking-spot")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("A1-244", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        responseBody = testClient
                .post()
                .uri("/api/v1/parking-spot")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }

    @Test
    public void createParkingSpot_existingParkingSpot_returnErrorMessageWithStatus409() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking-spot")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("A-01", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void createParkingSpot_withCustomerProfile_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/parking-spot")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jim@gmail.com", "123456"))
                .bodyValue(new ParkingSpotCreateDto("A-10", "FREE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void findParkingSpotByCode_withExistingCode_returnParkingSpotResponseDtoWithStatus200() {

        ParkingSpotResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/parking-spot/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParkingSpotResponseDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo("FREE");
            org.assertj.core.api.Assertions.assertThat(responseBody.getCode()).isEqualTo("A-01");

    }

    @Test
    public void findParkingSpotByCode_withNonExistentCode_returnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/parking-spot/A-50")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void findParkingSpotByCode_withCustomerProfile_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/parking-spot/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jim@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

}
