package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.support.AbstractResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


class ProductResourceTest extends AbstractResourceTest {

    private static final String CREATE_PRODUCT_URL = "/contracts/{id}/products";

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void should_create_product_success() {
        final Contract contract = Contract.builder()
                .depositPayed(true)
                .serviceChargeRate(100)
                .build();
        contractRepository.save(contract);

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
}