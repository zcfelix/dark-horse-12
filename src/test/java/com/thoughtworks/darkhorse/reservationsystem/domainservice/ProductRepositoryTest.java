package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("ci")
@ExtendWith(SpringExtension.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void should_save_product_success() {
        Product product = Product.builder()
                .name("noodle")
                .description("delicious")
                .price(10 * 100)
                .prepareMinutes(20)
                .build();
        Product saved = productRepository.save(product);
        assertNotNull(saved.getId());
        assertEquals(product.getName(), saved.getName());
    }

    @Test
    void should_save_product_with_price_10_01() {
        double priceInCent = 10.01 * 100;
        Integer price = (int) priceInCent;
        Product product = Product.builder()
                .name("noodle")
                .description("delicious")
                .price(price)
                .prepareMinutes(20)
                .build();
        Product saved = productRepository.save(product);
        assertNotNull(saved.getId());
        assertEquals(product.getName(), saved.getName());
    }
}