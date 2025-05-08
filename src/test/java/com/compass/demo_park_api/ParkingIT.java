package com.compass.demo_park_api;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotCreateDto;
import com.compass.demo_park_api.web.dto.CustomerParkingSpotResponseDto;
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
@Sql(scripts = "/sql/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void checkIn_withAdminProfileAndValidData_returnCustomerParkingSpotResponseDtoWithStatus201() {

        CustomerParkingSpotCreateDto dto = CustomerParkingSpotCreateDto.builder()
                .carModel("Vectra 2.0")
                .carBrand("Chevrolet")
                .licensePlateNumber("ABC-0000")
                .carColor("WHITE")
                .customerCpf("50339980052")
                .build();

        testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
            .bodyValue(dto)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().exists(HttpHeaders.LOCATION)
            .expectBody()
            .jsonPath("licensePlateNumber").isEqualTo("ABC-0000")
            .jsonPath("carModel").isEqualTo("Vectra 2.0")
            .jsonPath("carBrand").isEqualTo("Chevrolet")
            .jsonPath("carColor").isEqualTo("WHITE")
            .jsonPath("customerCpf").isEqualTo("50339980052")
            .jsonPath("receipt").exists()
            .jsonPath("entryDate").exists()
            .jsonPath("parkingSpotCode").exists();

    }

    @Test
    public void checkIn_withCustomerProfileAndValidData_returnErrorMessageWithStatus403() {

        CustomerParkingSpotCreateDto dto = CustomerParkingSpotCreateDto.builder()
                .carModel("Vectra 2.0")
                .carBrand("Chevrolet")
                .licensePlateNumber("ABC-0000")
                .carColor("WHITE")
                .customerCpf("50339980052")
                .build();

        testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jim@gmail.com", "123456"))
            .bodyValue(dto)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody()
            .jsonPath("status").isEqualTo(403)
            .jsonPath("path").isEqualTo("/api/v1/parking/checkIn")
            .jsonPath("method").isEqualTo("POST");

    }

    @Test
    public void checkIn_withInvalidData_returnErrorMessageWithStatus422() {

        CustomerParkingSpotCreateDto dto1 = CustomerParkingSpotCreateDto.builder()
            .carModel("Vectra 2.0")
            .carBrand("Chevrolet")
            .licensePlateNumber("ABC-00000")
            .carColor("WHITE")
            .customerCpf("047930260")
            .build();

        CustomerParkingSpotCreateDto dto2 = CustomerParkingSpotCreateDto.builder()
                .carModel("Vectra 2.0")
                .carBrand("Chevrolet")
                .licensePlateNumber("ABC-00")
                .carColor("WHITE")
                .customerCpf("047930260011")
                .build();

        testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
            .bodyValue(dto1)
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody()
            .jsonPath("status").isEqualTo(422)
            .jsonPath("path").isEqualTo("/api/v1/parking/checkIn")
            .jsonPath("method").isEqualTo("POST");


        testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
            .bodyValue(dto2)
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody()
            .jsonPath("status").isEqualTo(422)
            .jsonPath("path").isEqualTo("/api/v1/parking/checkIn")
            .jsonPath("method").isEqualTo("POST");


    }

    @Test
    public void checkIn_withNonExistsCpf_returnErrorMessageWithStatus404() {

        CustomerParkingSpotCreateDto dto = CustomerParkingSpotCreateDto.builder()
                .carModel("Vectra 2.0")
                .carBrand("Chevrolet")
                .licensePlateNumber("ABC-0000")
                .carColor("WHITE")
                .customerCpf("04793026001")
                .build();


        testClient
            .post()
            .uri("/api/v1/parking/checkIn")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
            .bodyValue(dto)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("status").isEqualTo(404)
            .jsonPath("path").isEqualTo("/api/v1/parking/checkIn")
            .jsonPath("method").isEqualTo("POST");

    }

    @Sql(scripts = "/sql/parking/parking-occupied-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-occupied-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void checkIn_returnErrorMessageWithStatus404() {

        CustomerParkingSpotCreateDto dto = CustomerParkingSpotCreateDto.builder()
                .carModel("Vectra 2.0")
                .carBrand("Chevrolet")
                .licensePlateNumber("ABC-0000")
                .carColor("WHITE")
                .customerCpf("60639545033")
                .build();


        testClient
                .post()
                .uri("/api/v1/parking/checkIn")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("path").isEqualTo("/api/v1/parking/checkIn")
                .jsonPath("method").isEqualTo("POST");

    }

    @Test
    public void findByReceipt_withExistsReceiptCustomerAndAdminProfiles_returnCustomerParkingSpotResponseDtoWithStatus200() {

        testClient
                .get()
                .uri("/api/v1/parking/checkIn/20250507-131100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "max@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("licensePlateNumber").isEqualTo("FIT-1020")
                .jsonPath("carModel").isEqualTo("Vectra 2.0")
                .jsonPath("carBrand").isEqualTo("Chevrolet")
                .jsonPath("carColor").isEqualTo("Black")
                .jsonPath("customerCpf").isEqualTo("60639545033")
                .jsonPath("receipt").isEqualTo("20250507-131100")
                .jsonPath("entryDate").isEqualTo("2025-05-07 13:11:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-02");


        testClient
                .get()
                .uri("/api/v1/parking/checkIn/20250507-131100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("licensePlateNumber").isEqualTo("FIT-1020")
                .jsonPath("carModel").isEqualTo("Vectra 2.0")
                .jsonPath("carBrand").isEqualTo("Chevrolet")
                .jsonPath("carColor").isEqualTo("Black")
                .jsonPath("customerCpf").isEqualTo("60639545033")
                .jsonPath("receipt").isEqualTo("20250507-131100")
                .jsonPath("entryDate").isEqualTo("2025-05-07 13:11:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-02");

    }

    @Test
    public void findByReceipt_withNonExistsReceipt_returnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/parking/checkIn/20250507-131101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void findByReceipt_withCheckOutAlreadyDone_returnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/parking/checkIn/20250507-131135")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Sql(scripts = "/sql/parking/parking-occupied-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-occupied-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void checkOut_withAdminProfileAndExistsReceipt_returnCustomerResponseDtoWithStatus200() {

        testClient
                .put()
                .uri("/api/v1/parking/checkOut/20250507-131109")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("receipt").isEqualTo("20250507-131109")
                .jsonPath("customerCpf").isEqualTo("60639545033")
                .jsonPath("licensePlateNumber").isEqualTo("FIT-1020")
                .jsonPath("entryDate").isEqualTo("2025-05-07 13:11:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-04")
                .jsonPath("carBrand").isEqualTo("Chevrolet")
                .jsonPath("carModel").isEqualTo("Vectra 2.0")
                .jsonPath("carColor").isEqualTo("Black")
                .jsonPath("exitDate").exists()
                .jsonPath("value").exists()
                .jsonPath("discount").exists();

    }

    @Test
    public void checkOut_withAdminProfileAndNonExistsReceipt_returnErrorMessageWithStatus404() {

        testClient
                .put()
                .uri("/api/v1/parking/checkOut/20250507-131102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("path").isEqualTo("/api/v1/parking/checkOut/20250507-131102")
                .jsonPath("method").isEqualTo("PUT")
                .jsonPath("status").isEqualTo(404);

    }

    @Sql(scripts = "/sql/parking/parking-occupied-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-occupied-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void checkOut_withAdminProfileAndCheckOutAlreadyDone_returnErrorMessageWithStatus404() {

        testClient
                .put()
                .uri("/api/v1/parking/checkOut/20250507-141109")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "mario@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("path").isEqualTo("/api/v1/parking/checkOut/20250507-141109")
                .jsonPath("method").isEqualTo("PUT")
                .jsonPath("status").isEqualTo(404);

    }

    @Test
    public void checkOut_withCustomerProfile_returnErrorMessageWithStatus403() {

        testClient
                .put()
                .uri("/api/v1/parking/checkOut/20250507-131103")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "max@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("method").isEqualTo("PUT")
                .jsonPath("status").isEqualTo(403)
                .jsonPath("path").isEqualTo("/api/v1/parking/checkOut/20250507-131103");

    }

}
