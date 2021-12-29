package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("ci")
@ExtendWith(SpringExtension.class)
class ProductRepositoryImplTest {

    @Inject
    private ProductRepositoryImpl productRepository;

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

        Page<Product> page = productRepository.findAll(0, 10);
        assertEquals(5, page.getElements().size());
        assertEquals(5, page.getTotalSize());
        assertTrue(page.getElements().stream().anyMatch(product -> product.getName().equals("noodle-1")));
    }

    @Test
    void should_list_products_by_page_success_when_products_total_size_over_page_size() {
        List<Product> products = IntStream.rangeClosed(1, 11).mapToObj(i -> Product.builder()
                .name("noodle-" + i)
                .description("delicious")
                .price(i * 100)
                .prepareMinutes(i)
                .build()).collect(toList());
        productRepository.saveAllAndFlush(products);

        Page<Product> page = productRepository.findAll(0, 10);
        assertEquals(11, page.getTotalSize());
        assertEquals(10, page.getElements().size());
        assertTrue(page.getElements().stream().anyMatch(product -> product.getName().equals("noodle-1")));
    }

    @Test
    void should_list_empty_when_there_are_no_products() {
        Page<Product> page = productRepository.findAll(0, 10);
        assertEquals(0, page.getTotalSize());
        assertEquals(0, page.getElements().size());
    }
}