package com.compass.demo_park_api;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.web.dto.CustomerCreateDto;
import com.compass.demo_park_api.web.dto.CustomerResponseDto;
import com.compass.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/customers/customers-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/customers/customers-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CustomerIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void createCustomer_ValidData_returnCustomerResponseDtoWithStatus201() {

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
    public void createCustomer_withExistingCustomer_returnErrorMessageWithStatus409() {

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
    public void createCustomer_InvalidData_returnErrorMessageWithStatus422() {

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
                .bodyValue(new CustomerCreateDto("12345678901", "abcd"))
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
                .bodyValue(new CustomerCreateDto("047.930.260-01", "abc"))
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createCustomer_AdminUser_returnErrorMessageWithStatus403() {

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

    @Test
    public void findCustomerById_validIdByAdmin_returnCustomerResponseDtoWithStatus200() {

        CustomerResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/customers/203")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(203);

    }

    @Test
    public void findCustomerById_invalidIdByAdmin_returnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/customers/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void findCustomerById_validIdByCustomer_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/customers/203")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void findAllCustomers_withAdminProfile_returnListCustomerResponseDtoWithStatus200() {

        List<CustomerResponseDto> responseBody = testClient
                .get()
                .uri("/api/v1/customers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponseDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody).size().isEqualTo(1);
    }

    @Test
    public void findAllCustomers_withCustomerProfile_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/customers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getDetails_withAdminProfile_returnErrorMessageWithStatus403() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/customers/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void getDetails_withCustomerProfile_returnCustomerResponseDtoWithStatus200() {

        CustomerResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/customers/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "pen@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("60639545033");
            org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Pen Purple");
            org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(203);

    }

}
