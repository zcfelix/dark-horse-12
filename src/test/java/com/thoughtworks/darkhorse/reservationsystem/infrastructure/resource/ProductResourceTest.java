package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.command.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ProductRepository;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource.support.AbstractResourceTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;


class ProductResourceTest extends AbstractResourceTest {

    private static final String CREATE_PRODUCT_URL = "/contracts/{id}/products";
    private static final String LIST_PRODUCTS_URL = "/products";

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ProductRepository productRepository;

    private Contract payedContract;
    private Contract notPayedContract;

    @BeforeEach
    void setUp() {
        Contract payed = Contract.builder()
                .depositPayed(true)
                .serviceChargeRate(100)
                .build();
        payedContract = contractRepository.save(payed);

        Contract notPayed = Contract.builder()
                .depositPayed(false)
                .serviceChargeRate(100)
                .build();
        notPayedContract = contractRepository.save(notPayed);
    }

    @AfterEach
    void tearDown() {
        contractRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void should_create_product_success() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 10.0d, 20);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, payedContract.getId())
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
                .post(CREATE_PRODUCT_URL, payedContract.getId())
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
                .post(CREATE_PRODUCT_URL, payedContract.getId())
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
                .post(CREATE_PRODUCT_URL, payedContract.getId())
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

    @Test
    void should_create_product_failed_when_create_product_with_non_existed_contract() {
        final CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 1d, 1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createProductCommand)
                .post(CREATE_PRODUCT_URL, "non_existed_id")
                .then()
                .statusCode(404)
                .body("code", is("CONTRACT_NOT_EXIST"))
                .body("timestamp", notNullValue())
                .body("data.contractId", is("non_existed_id"))
                .body("path", notNullValue());
    }

    @Test
    void should_list_products_by_page_success() {
        List<Product> products = IntStream.rangeClosed(1, 5).mapToObj(i -> Product.builder()
                .name("noodle-" + i)
                .description("delicious")
                .price(i * 100)
                .prepareMinutes(i)
                .build()).collect(toList());
        productRepository.saveAllAndFlush(products);

        given()
                .when()
                .queryParam("pageIndex", 0)
                .queryParam("pageSize", 10)
                .get(LIST_PRODUCTS_URL)
                .then()
                .statusCode(200)
                .body("contents.size()", is(5))
                .body("totalSize", is(5));
    }
}