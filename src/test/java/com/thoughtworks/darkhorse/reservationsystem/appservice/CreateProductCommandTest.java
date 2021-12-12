package com.thoughtworks.darkhorse.reservationsystem.appservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateProductCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_throw_error_message_when_create_product_with_negative_price() {
        CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", -1d, 1);
        Set<ConstraintViolation<CreateProductCommand>> violations = validator.validate(createProductCommand);
        assertEquals(1, violations.size());

        assertEquals("product price should not be negative",
                violations.stream().map(ConstraintViolation::getMessage).findFirst().get());
    }
}