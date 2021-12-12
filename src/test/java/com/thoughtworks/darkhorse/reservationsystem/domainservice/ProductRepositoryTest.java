package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("ci")
@ExtendWith(SpringExtension.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

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

    @Test
    void should_list_products_by_page_success() {
        List<Product> products = IntStream.rangeClosed(1, 5).mapToObj(i -> Product.builder()
                .name("noodle-" + i)
                .description("delicious")
                .price(i * 100)
                .prepareMinutes(i)
                .build()).collect(toList());
        productRepository.saveAllAndFlush(products);

        Page<Product> page = productRepository.findAll(PageRequest.of(0, 10));
        assertEquals(5, page.getTotalElements());
        assertEquals(5, page.getContent().size());
        assertTrue(page.getContent().stream().anyMatch(product -> product.getName().equals("noodle-1")));
    }
}