package com.compass.demo_park_api;

import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerResponseDto;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/customers/customers-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/customers/customers-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CustomerIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void createCustomer_ValidData_returnStatus201() {

        CustomerResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("04793026001", "Jim Brown"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jim@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Jim Brown");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("04793026001");

    }

    @Test
    public void createCustomer_withExistingCustomer_returnWithStatus409() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("60639545033", "Pen Purple"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void createCustomer_InvalidData_returnWithStatus422() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("", ""))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("12345678901", "abc"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("047930260011", "abc"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createCustomer_AdminUser_returnWithStatus403() {

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/customers")
                .bodyValue(new CustomerCreateDto("32006440012", "Jose Pereira"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
