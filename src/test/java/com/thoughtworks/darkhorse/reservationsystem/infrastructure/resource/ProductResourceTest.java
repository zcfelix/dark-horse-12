package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.support.AbstractResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


class ProductResourceTest extends AbstractResourceTest {

    private static final String CREATE_PRODUCT_URL = "/contracts/{id}/products";

    @Autowired
    private ContractRepository contractRepository;

    private Contract contract;
    private Contract notPayedContract;

    @BeforeEach
    void setUp() {
        contract = Contract.builder()
                .depositPayed(true)
                .serviceChargeRate(100)
                .build();
        contractRepository.save(contract);

        notPayedContract = Contract.builder()
                .depositPayed(false)
                .serviceChargeRate(100)
                .build();
        contractRepository.save(notPayedContract);
    }

    @Test
    void should_create_product_success() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 10.0d, 20);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, contract.getId())
                .then()
                .statusCode(201)
                .body("uri", matchesPattern("^/products/.*$"))
                .body("name", is("noodle"))
                .body("description", is("delicious"))
                .body("price", is(10.0f))
                .body("prepareMinutes", is(20))
                .body("earning", is(9.9f));
    }

    @Test
    void should_create_product_failed_with_negative_price() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", -1d, 1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, contract.getId())
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("timestamp", notNullValue())
                .body("error", is("Bad Request"))
                .body("path", notNullValue());
    }

    @Test
    void should_create_product_failed_with_negative_prepare_time() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 1d, -1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, contract.getId())
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("timestamp", notNullValue())
                .body("error", is("Bad Request"))
                .body("path", notNullValue());
    }

    @Test
    void should_create_product_failed_with_negative_price_and_negative_prepare_time() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", -1d, -1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, contract.getId())
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("timestamp", notNullValue())
                .body("error", is("Bad Request"))
                .body("path", notNullValue());
    }

    @Test
    void should_create_product_failed_before_deposit_paying() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 1d, 1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, notPayedContract.getId())
                .then()
                .statusCode(400)
                .body("code", is("DEPOSIT_NOT_PAYED"))
                .body("timestamp", notNullValue())
                .body("data.contractId", is(notPayedContract.getId()))
                .body("path", notNullValue());
    }
}